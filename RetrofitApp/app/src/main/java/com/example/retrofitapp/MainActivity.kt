package com.example.retrofitapp

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

// Definición de la clase MainActivity que extiende AppCompatActivity e implementa SearchView.OnQueryTextListener
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    // Declaración de variables
    var binding: ActivityMainBinding? = null
    var dogAdapter: DogAdapter? = null
    var images: MutableList<String> = ArrayList()

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el diseño de la actividad y establecerlo como el contenido de la vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // Inicializar el RecyclerView y el adaptador
        initRecyclerView()
        // Configurar el listener de búsqueda en el SearchView
        binding!!.searchDogs.setOnQueryTextListener(this as SearchView.OnQueryTextListener)
    }

    // Método privado para inicializar el RecyclerView
    private fun initRecyclerView() {
        // Crear una instancia del adaptador de perros con la lista de imágenes vacía
        dogAdapter = DogAdapter(images)
        // Establecer un LinearLayoutManager en el RecyclerView
        binding!!.listDogs.layoutManager = LinearLayoutManager(this)
        // Establecer el adaptador en el RecyclerView
        binding!!.listDogs.adapter = dogAdapter
    }

    // Método para obtener una instancia del servicio de la API mediante Retrofit
    private val apiService: ApiService
        private get() {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dog.ceo/api/breed/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }

    // Método para realizar la búsqueda de imágenes de perros por raza
    private fun searchByName(raza: String) {
        // Llamar al método correspondiente en el servicio de la API para obtener la lista de imágenes
        val batch: Call<DogsResponse?>? = apiService.getDogsByBreed(raza)
        batch?.enqueue(object : Callback<DogsResponse?> {
            // Manejar la respuesta exitosa
            override fun onResponse(
                @Nullable call: Call<DogsResponse?>?,
                @Nullable response: Response<DogsResponse?>?
            ) {
                if (response != null && response.body() != null) {
                    // Obtener la lista de imágenes de la respuesta
                    val responseImages: List<String> = response.body()!!.getImages() as List<String>
                    // Limpiar la lista actual de imágenes y agregar las nuevas
                    images.clear()
                    images.addAll(responseImages)
                    // Notificar al adaptador que los datos han cambiado
                    dogAdapter!!.notifyDataSetChanged()
                }
            }

            // Manejar la respuesta fallida
            override fun onFailure(@Nullable call: Call<DogsResponse?>?, @Nullable t: Throwable?) {
                if (t != null) {
                    // Mostrar un mensaje de error en caso de falla
                    showError()
                }
            }
        })
    }

    // Método para mostrar un mensaje de error
    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    // Método que se llama cuando se envía un texto de búsqueda
    override fun onQueryTextSubmit(query: String): Boolean {
        if (!query.isEmpty()) {
            // Convertir la consulta a minúsculas y realizar la búsqueda
            searchByName(query.lowercase(Locale.getDefault()))
        }
        return true
    }

    // Método que se llama cuando cambia el texto de búsqueda (no utilizado en este caso)
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}
