package com.example.sqliteapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteapp.db.HelperDB
import com.example.sqliteapp.model.Categoria
import com.example.sqliteapp.model.Productos

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var managerCategoria: Categoria? = null
    private var managerProductos: Productos? = null
    private var dbHelper: HelperDB? = null
    private var db: SQLiteDatabase? = null
    private var cursor: Cursor? = null
    private var txtIdDB: TextView? = null
    private var txtId: EditText? = null
    private var txtNombre: EditText? = null
    private var txtPrecio: EditText? = null
    private var txtCantidad: EditText? = null
    private var cmbCategorias: Spinner? = null
    private var btnAgregar: Button? = null
    private var btnActualizar: Button? = null
    private var btnEliminar: Button? = null
    private var btnBuscar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtIdDB = findViewById(R.id.txtIdDB)
        txtId = findViewById(R.id.txtId)
        txtNombre = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtCantidad = findViewById(R.id.txtCantidad)
        cmbCategorias = findViewById<Spinner>(R.id.cmbCategorias)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnBuscar = findViewById(R.id.btnBuscar)
        dbHelper = HelperDB(this)
        db = dbHelper!!.writableDatabase
        setSpinnerCategorias()
        btnAgregar!!.setOnClickListener(this)
        btnActualizar!!.setOnClickListener(this)
        btnEliminar!!.setOnClickListener(this)
        btnBuscar!!.setOnClickListener(this)
    }

    fun setSpinnerCategorias() {
        // Cargando valores por defecto
        managerCategoria = Categoria(this)
        managerCategoria!!.insertValuesDefault()
        cursor = managerCategoria!!.showAllCategoria()
        val cat = ArrayList<String>()
        if (cursor != null && cursor!!.count > 0) {
            cursor!!.moveToFirst()
            cat.add(cursor!!.getString(1))
            do {
                cat.add(cursor!!.getString(1))
            } while (cursor!!.moveToNext())
        }
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, cat)

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cmbCategorias!!.adapter = adaptador
    }

    override fun onClick(view: View) {
        managerProductos = Productos(this)
        val nombre: String = txtNombre!!.text.toString().trim()
        val precio: String = txtPrecio!!.text.toString().trim()
        val cantidad: String = txtCantidad!!.text.toString().trim()
        val categoria: String = cmbCategorias!!.selectedItem.toString().trim()
        val idcategoria = managerCategoria!!.searchID(categoria)
        val idproducto = txtId!!.text.toString().trim()
        if (db != null) {
            if (view === btnAgregar) {
                if (vericarFormulario("insertar")) {
                    managerProductos!!.addNewProducto(
                        idcategoria,
                        nombre,
                        precio.toDouble(),
                        cantidad.toInt()
                    )
                    Toast.makeText(
                        this, "Producto agregado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (view === btnActualizar) {
                if (vericarFormulario("actualizar")) {
                    managerProductos!!.updateProducto(
                        idproducto.toInt(),
                        idcategoria,
                        nombre,
                        precio.toDouble(),
                        cantidad.toInt()
                    )
                    Toast.makeText(
                        this, "Producto actualizado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (view === btnEliminar) {
                if (vericarFormulario("eliminar")) {
                    managerProductos!!.deleteProducto(idproducto.toInt())
                    Toast.makeText(
                        this, "Producto eliminado",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (view === btnBuscar) {
                // IMPLEMENTACIÓN DE LA FUNCIÓN DE BÚSQUEDA
                if (vericarFormulario("buscar")) {
                    val idproducto = txtId!!.text.toString().trim().toInt()
                    // Buscar el producto por ID
                    val cursor: Cursor? = managerProductos!!.searchProducto(idproducto)
                    if (cursor != null && cursor.moveToFirst()) {
                        // Si se encuentra el producto, cargar los datos en los campos
                        val idcategoria = cursor.getInt(cursor.getColumnIndexOrThrow(Productos.COL_IDCATEGORIA))
                        val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(Productos.COL_DESCRIPCION))
                        val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(Productos.COL_PRECIO))
                        val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(Productos.COL_CANTIDAD))

                        // Mostrar los datos en los campos
                        txtNombre!!.setText(descripcion)
                        txtPrecio!!.setText(precio.toString())
                        txtCantidad!!.setText(cantidad.toString())

                        // Seleccionar la categoría en el Spinner
                        val categoriaNombre = managerCategoria!!.searchNombre(idcategoria)
                        val spinnerAdapter = cmbCategorias!!.adapter as ArrayAdapter<String>
                        val categoriaPosition = spinnerAdapter.getPosition(categoriaNombre)
                        cmbCategorias!!.setSelection(categoriaPosition)

                        Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show()
                    } else {
                        // Si no se encuentra el producto, mostrar un mensaje de error
                        Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(
                    this, "No se puede conectar a la Base de Datos",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun vericarFormulario(opc: String): Boolean {
        var notificacion: String = "Se han generado algunos errores, favor verifiquelos"
        var response = true
        var idproducto_v = true
        var idcategoria_v = true
        var nombre_v = true
        var precio_v = true
        var cantidad_v = true
        val nombre: String = txtNombre!!.text.toString().trim()
        val precio: String = txtPrecio!!.text.toString().trim()
        val cantidad: String = txtCantidad!!.text.toString().trim()
        val categoria: String = cmbCategorias!!.selectedItem.toString().trim()
        val idproducto: String = txtId!!.text.toString().trim()
        if (opc === "insertar" || opc == "actualizar") {
            if (nombre.isEmpty()) {
                txtNombre!!.error = "Ingrese el nombre del producto"
                txtNombre!!.requestFocus()
                nombre_v = false
            }
            if (precio.isEmpty()) {
                txtPrecio!!.error = "Ingrese el precio del producto"
                txtPrecio!!.requestFocus()
                precio_v = false
            }
            if (cantidad.isEmpty()) {
                txtCantidad!!.error = "Ingrese la cantidad inicial"
                txtCantidad!!.requestFocus()
                cantidad_v = false
            }
            if (opc == "actualizar") {
                if (idproducto.isEmpty()) {
                    idproducto_v = false
                    notificacion = "No se ha seleccionado un producto"
                }
                response =
                    !(nombre_v == false || precio_v == false || cantidad_v == false || idproducto_v == false)
            } else {
                response = !(nombre_v == false || precio_v == false || cantidad_v == false)
            }
        } else if (opc === "eliminar" || opc == "buscar") {
            if (idproducto.isEmpty()) {
                response = false
                notificacion = "No se ha seleccionado un producto"
            }
        }
        // Mostrar errores
        if (response == false) {
            Toast.makeText(
                this,
                notificacion,
                Toast.LENGTH_LONG
            ).show()
        }
        return response
    }
}