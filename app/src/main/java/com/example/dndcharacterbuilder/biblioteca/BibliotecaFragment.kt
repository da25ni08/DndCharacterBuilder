package com.example.dndcharacterbuilder.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dndcharacterbuilder.api.ApiService
import com.example.dndcharacterbuilder2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BibliotecaFragment(private val section: String) : Fragment() {

    private lateinit var scrollView: android.widget.ScrollView
    private lateinit var linearLayoutContent: LinearLayout
    private lateinit var etBusqueda: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biblioteca, container, false)

        // Encontramos las vistas necesarias
        scrollView = view.findViewById(R.id.scrollView)
        linearLayoutContent = view.findViewById(R.id.linearLayoutContent)

        // Hacer las llamadas API según el botón presionado
        fetchItemsFromApi(section, null)
        etBusqueda = view.findViewById(R.id.etBusqueda)

        return view
    }

    private fun fetchItemsFromApi(section: String, busqueda: String?) {
        // Limpiar el contenido previo
        linearLayoutContent.removeAllViews()

        CoroutineScope(Dispatchers.IO).launch {
            // Llamada a la API según la sección seleccionada
            val response = when (section) {
                "spells" -> ApiService.create().getSpells(busqueda)
                "feats" -> ApiService.create().getFeats(busqueda)
                "equipment" -> ApiService.create().getEquipment(busqueda)
                "classes" -> ApiService.create().getClassesItem(busqueda)
                else -> throw IllegalArgumentException("Invalid section")
            }

            if (response.isSuccessful) {
                val itemList = response.body()?.results ?: listOf()

                // Volver al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    for (item in itemList) {
                        // Crear TextViews dinámicamente para cada ítem
                        val textView = TextView(requireContext())
                        textView.text = item.name  // O ajusta lo que quieras mostrar
                        textView.setPadding(16, 16, 16, 16)

                        // Añadir el TextView al LinearLayout dentro del ScrollView
                        linearLayoutContent.addView(textView)
                    }
                }
            } else {
                // Manejo del error si la respuesta de la API no fue exitosa
                withContext(Dispatchers.Main) {
                    val errorView = TextView(requireContext())
                    errorView.text = "Error al cargar los datos."
                    linearLayoutContent.addView(errorView)
                }
            }
        }
    }

    public fun buscar(v:View){
        fetchItemsFromApi(section, etBusqueda.text.toString().trim())
    }
}


