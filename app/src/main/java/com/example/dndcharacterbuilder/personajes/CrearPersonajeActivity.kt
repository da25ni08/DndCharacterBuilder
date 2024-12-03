package com.example.dndcharacterbuilder.personajes

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharacterbuilder.api.ApiService
import com.example.dndcharacterbuilder.api.model.Class
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.dndcharacterbuilder2.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearPersonajeActivity : AppCompatActivity() {

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
    private var characterClasses = mutableListOf<Class>()
    private var races: List<String> = emptyList()

    private var usuario:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_personaje)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var bundle = intent.extras
        usuario = bundle?.getString("user")

        // Inicializamos las vistas
        etCharacterName = findViewById(R.id.etCharacterName)
        etLevel = findViewById(R.id.etLevel)
        spinnerClass = findViewById(R.id.spinnerClass)
        spinnerRace = findViewById(R.id.spinnerRace)
        btnSaveCharacter = findViewById(R.id.btnSaveCharacter)

        personajeDAO = PersonajeDAO(this)

        // Llenar el spinner de razas (solicitamos las razas de la API)
        fetchClasses()
        fetchRaces()

        // Manejo del botón para guardar el personaje
        btnSaveCharacter.setOnClickListener {
            val characterName = etCharacterName.text.toString()
            val selectedClassName = spinnerClass.selectedItem.toString()

            // Buscar la clase que tiene el mismo nombre
            val selectedClass = characterClasses.find { it.name == selectedClassName }

            val selectedRace = spinnerRace.selectedItem.toString() // Raza seleccionada
            val selectedLevel = etLevel.text.toString().toIntOrNull() ?: 1 // Nivel seleccionado

            // Crear el personaje con los datos
            val newCharacter = selectedClass?.let { it1 ->
                Personaje(
                    name = characterName,
                    charClass = it1.name,
                    level = selectedLevel,
                    race = selectedRace,
                    classUrl = selectedClass.url, // Asignamos la URL de la clase
                    raceUrl = "", // Puedes agregar una URL de raza si tienes
                    background = "", // Puedes agregar background si tienes
                    backgroundUrl = "", // Puedes agregar URL de background si tienes
                    hitDie = 0, // Asigna un valor adecuado según lo que necesites
                    userId = usuario!! // Asegúrate de usar el ID adecuado
                )
            }

            // Guardar el personaje en la base de datos
            if (newCharacter != null) {
                personajeDAO.insertCharacter(newCharacter)
            }else{
                Toast.makeText(this, "Ha ocurrido un error, intentelo otra vez", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, PersonajesActivity::class.java).apply {
                putExtra("user", usuario)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }



    private fun fetchClasses() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dndApiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getClasses() // Llamada a la API para obtener las clases
                if (response.isSuccessful) {
                    val classesList = response.body()?.results // Esta es una lista de objetos 'Class'

                    runOnUiThread {
                        // Verificamos si las clases se recibieron correctamente
                        classesList?.let {
                            characterClasses.clear()
                            characterClasses.addAll(it) // Añadimos todas las clases

                            // Extraemos solo los nombres de las clases para el Spinner
                            val classNames = it.map { it.name }

                            // Creamos el adaptador para el Spinner con los nombres de las clases
                            val classAdapter = ArrayAdapter(this@CrearPersonajeActivity, android.R.layout.simple_spinner_item, classNames)
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
                        val raceAdapter = ArrayAdapter(this@CrearPersonajeActivity, android.R.layout.simple_spinner_item, races)
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
