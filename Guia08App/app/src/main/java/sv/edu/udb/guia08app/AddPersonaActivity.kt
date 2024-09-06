package sv.edu.udb.guia08app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sv.edu.udb.guia08app.PersonasActivity.Companion.database
import sv.edu.udb.guia08app.datos.Persona

class AddPersonaActivity : AppCompatActivity() {
    private var edtDUI: EditText? = null
    private var edtNombre: EditText? = null
    private var key = ""
    private var nombre = ""
    private var dui = ""
    private var accion = ""
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_persona)
        inicializar()
    }

    private fun inicializar() {
        edtNombre = findViewById<EditText>(R.id.edtNombre)
        edtDUI = findViewById<EditText>(R.id.edtDUI)
        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDUI = findViewById<EditText>(R.id.edtDUI)
// Obtención de datos que envia actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key = datos.getString("key").toString()
        }
        if (datos != null) {
            edtDUI.setText(intent.getStringExtra("dui").toString())
        }
        if (datos != null) {
            edtNombre.setText(intent.getStringExtra("nombre").toString())
        }
        if (datos != null) {
            accion = datos.getString("accion").toString()
        }
    }

    fun guardar(v: View?) {
        val nombre: String = edtNombre?.text.toString()
        val dui: String = edtDUI?.text.toString()
        database = FirebaseDatabase.getInstance().getReference("personas")
        // Se forma objeto persona
        val persona = Persona(dui, nombre)
        if (accion == "a") { //Agregar registro
            database.child(nombre).setValue(persona).addOnSuccessListener {
                Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
        } else // Editar registro
        {
            val key = database.child("nombre").push().key
            if (key == null) {
                Toast.makeText(this, "Llave vacia", Toast.LENGTH_SHORT).show()
            }
            val personasValues = persona.toMap()
            val childUpdates = hashMapOf<String, Any>(
                "$nombre" to personasValues
            )
            database.updateChildren(childUpdates)
            Toast.makeText(this, "Se actualizo con exito", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    fun cancelar(v: View?) {
        finish()
    }
}