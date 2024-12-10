package com.example.dndcharacterbuilder.biblioteca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dndcharacterbuilder.api.ApiService
import com.example.dndcharacterbuilder2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class BibliotecaFragment(private val section: String) : Fragment() {

    private lateinit var scrollView: android.widget.ScrollView
    private lateinit var linearLayoutContenido: LinearLayout
    private lateinit var inputBusqueda: EditText
    private lateinit var btnBusqedaVoz: FloatingActionButton

    private val SPEECH_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_biblioteca, container, false)

        scrollView = vista.findViewById(R.id.scrollView)
        linearLayoutContenido = vista.findViewById(R.id.linearLayoutContenido)
        inputBusqueda = vista.findViewById(R.id.inputBusqueda)
        btnBusqedaVoz = vista.findViewById(R.id.floatingBusquedaVoz)

        fetchItemsFromApi(section, null)

        vista.findViewById<ImageButton>(R.id.btnBuscar).setOnClickListener {
            val busqueda = inputBusqueda.text.toString().trim()
            if (busqueda.isEmpty()) {
                fetchItemsFromApi(section, null)
            } else {
                fetchItemsFromApi(section, busqueda)
            }
        }

        vista.findViewById<ImageButton>(R.id.btnResetear).setOnClickListener {
            inputBusqueda.text.clear()
            fetchItemsFromApi(section, null)
        }

        btnBusqedaVoz.setOnClickListener {
            startSpeechToText()
        }

        return vista
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

            putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga la palabra que quiere buscar")
        }
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(), "El reconocimiento de voz no está disponible",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            result?.let {
                fetchItemsFromApi(section, result[0])
            }
        }
    }

    private fun fetchItemsFromApi(section: String, busqueda: String?) {
        linearLayoutContenido.removeAllViews()

        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = when (section) {
                "spells" -> ApiService.create().getSpells(busqueda)
                "feats" -> ApiService.create().getFeats(busqueda)
                "equipment" -> ApiService.create().getEquipment(busqueda)
                "classes" -> ApiService.create().getClassesItem(busqueda)
                else -> throw IllegalArgumentException("Invalid section")
            }

            if (respuesta.isSuccessful) {
                val listaItems = respuesta.body()?.results ?: listOf()

                withContext(Dispatchers.Main) {
                    if (listaItems.isEmpty()) {
                        Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT)
                            .show()
                        val errorView = TextView(requireContext())
                        errorView.text = "No results found for this search."
                        linearLayoutContenido.addView(errorView)
                    } else {
                        for (item in listaItems) {
                            val textView = TextView(requireContext())
                            textView.text = item.name
                            textView.setPadding(16, 16, 16, 16)
                            linearLayoutContenido.addView(textView)
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    val errorView = TextView(requireContext())
                    errorView.text = "Error loading data."
                    linearLayoutContenido.addView(errorView)
                }
            }
        }
    }
}



