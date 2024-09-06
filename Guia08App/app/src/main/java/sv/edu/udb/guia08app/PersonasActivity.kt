package sv.edu.udb.guia08app

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import sv.edu.udb.guia08app.datos.Persona

class PersonasActivity : AppCompatActivity() {
    // Ordenamiento para hacer la consultas a los datos
    var consultaOrdenada: Query = refPersonas.orderByChild("nombre")
    var personas: MutableList<Persona>? = null
    var listaPersonas: ListView? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personas)
        inicializar()
    }
    private fun inicializar() {
        val fab_agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_agregar)
        listaPersonas = findViewById<ListView>(R.id.ListaPersonas)
        // Cuando el usuario haga clic en la lista (para editar registro)
        listaPersonas!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), AddPersonaActivity::class.java)
                intent.putExtra("accion", "e") // Editar
                intent.putExtra("key", personas!![i].key)
                intent.putExtra("nombre", personas!![i].nombre)
                intent.putExtra("dui", personas!![i].dui)
                startActivity(intent)
            }
        })
        // Cuando el usuario hace un LongClic (clic sin soltar elemento por mas de 2 segundos)
        // Es por que el usuario quiere eliminar el registro
        listaPersonas!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                // Preparando cuadro de dialogo para preguntar al usuario
                // Si esta seguro de eliminar o no el registro
                val ad = AlertDialog.Builder(this@PersonasActivity)
                ad.setMessage("Está seguro de eliminar registro?")
                    .setTitle("Confirmación")
                ad.setPositiveButton("Si"
                ) { dialog, id ->
                    personas!![position].nombre?.let {
                        refPersonas.child(it).removeValue()
                    }
                    Toast.makeText(
                        this@PersonasActivity,
                        "Registro borrado!", Toast.LENGTH_SHORT
                    ).show()
                }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        Toast.makeText(
                            this@PersonasActivity,
                            "Operación de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                ad.show()
                return true
            }
        }
        fab_agregar.setOnClickListener(View.OnClickListener { // Cuando el usuario quiere agregar un nuevo registro
            val i = Intent(getBaseContext(), AddPersonaActivity::class.java)
            i.putExtra("accion", "a") // Agregar
            i.putExtra("key", "")
            i.putExtra("nombre", "")
            i.putExtra("dui", "")
            startActivity(i)
        })
        personas = ArrayList<Persona>()
        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                personas!!.removeAll(personas!!)
                for (dato in dataSnapshot.getChildren()) {
                    val persona: Persona? = dato.getValue(Persona::class.java)
                    persona?.key(dato.key)
                    if (persona != null) {
                        personas!!.add(persona)
                    }
                }
                val adapter = AdaptadorPersona(
                    this@PersonasActivity,
                    personas as ArrayList<Persona>
                )
                listaPersonas!!.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refPersonas: DatabaseReference = database.getReference("personas")
    }
}