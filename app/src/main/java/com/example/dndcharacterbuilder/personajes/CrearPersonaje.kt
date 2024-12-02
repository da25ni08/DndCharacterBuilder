package com.example.dndcharacterbuilder.personajes

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharachterbuilder.personajes.Personaje
import com.example.dndcharachterbuilder.personajes.PersonajeDAO
import com.example.dndcharacterbuilder.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.dndcharacterbuilder2.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearPersonaje : AppCompatActivity() {

    private lateinit var etCharacterName: EditText
    private lateinit var etStrength: EditText
    private lateinit var etDexterity: EditText
    private lateinit var etConstitution: EditText
    private lateinit var etIntelligence: EditText
    private lateinit var etWisdom: EditText
    private lateinit var etCharisma: EditText
    private lateinit var etLevel: EditText
    private lateinit var spinnerClass: Spinner
    private lateinit var spinnerRace: Spinner
    private lateinit var btnSaveCharacter: Button

    private lateinit var personajeDAO: PersonajeDAO
    private lateinit var dndApiService: ApiService
    private val characterClasses = mutableListOf<String>()
    private var races: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_personaje)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializamos las vistas
        etCharacterName = findViewById(R.id.etCharacterName)
        etStrength = findViewById(R.id.etStrength)
        etDexterity = findViewById(R.id.etDexterity)
        etConstitution = findViewById(R.id.etConstitution)
        etIntelligence = findViewById(R.id.etIntelligence)
        etWisdom = findViewById(R.id.etWisdom)
        etCharisma = findViewById(R.id.etCharisma)
        etLevel = findViewById(R.id.etLevel)
        spinnerClass = findViewById(R.id.spinnerClass)
        spinnerRace = findViewById(R.id.spinnerRace)
        btnSaveCharacter = findViewById(R.id.btnSaveCharacter)

        personajeDAO = PersonajeDAO(this)

        // Llenar los spinners de clase
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, characterClasses)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClass.adapter = classAdapter

        // Llenar el spinner de razas (solicitamos las razas de la API)
        fetchClasses()
        fetchRaces()

        // Manejo del botón para guardar el personaje
        btnSaveCharacter.setOnClickListener {
            val characterName = etCharacterName.text.toString()
            val selectedClass = spinnerClass.selectedItem.toString()
            val selectedRace = spinnerRace.selectedItem.toString()
            val selectedStrength = etStrength.text.toString().toIntOrNull() ?: 8
            val selectedDexterity = etDexterity.text.toString().toIntOrNull() ?: 8
            val selectedConstitution = etConstitution.text.toString().toIntOrNull() ?: 8
            val selectedIntelligence = etIntelligence.text.toString().toIntOrNull() ?: 8
            val selectedWisdom = etWisdom.text.toString().toIntOrNull() ?: 8
            val selectedCharisma = etCharisma.text.toString().toIntOrNull() ?: 8
            val selectedLevel = etLevel.text.toString().toIntOrNull() ?: 1

            val newCharacter = Personaje(
                name = characterName,
                charClass = selectedClass,
                level = selectedLevel,
                race = selectedRace
            )

            // Guardar el personaje en la base de datos
            personajeDAO.insertCharacter(newCharacter)
        }
    }

    // Método para obtener las clases desde la API
    private fun fetchClasses() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dndApiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getClasses()
                if (response.isSuccessful) {
                    val classesList = response.body()?.results?.map { it.name }
                    runOnUiThread {
                        classesList?.let {
                            characterClasses.clear()
                            characterClasses.addAll(it)
                            val classAdapter = ArrayAdapter(this@CrearPersonaje, android.R.layout.simple_spinner_item, characterClasses)
                            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerClass.adapter = classAdapter
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Método para obtener las razas desde la API
    private fun fetchRaces() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getRaces()
                if (response.isSuccessful) {
                    val racesList = response.body()?.results?.map { it.name }
                    runOnUiThread {
                        races = racesList ?: emptyList()
                        val raceAdapter = ArrayAdapter(this@CrearPersonaje, android.R.layout.simple_spinner_item, races)
                        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerRace.adapter = raceAdapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
