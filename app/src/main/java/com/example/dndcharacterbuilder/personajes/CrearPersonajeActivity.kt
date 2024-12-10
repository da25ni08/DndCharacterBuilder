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
import com.example.dndcharacterbuilder.api.model.Background
import com.example.dndcharacterbuilder.api.model.Class
import com.example.dndcharacterbuilder.api.model.Race
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.dndcharacterbuilder2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearPersonajeActivity : AppCompatActivity() {

    private lateinit var inputNombrePersonaje: EditText
    private lateinit var etLevel: EditText
    private lateinit var spinnerClass: Spinner
    private lateinit var spinnerRace: Spinner
    private lateinit var spinnerBackground: Spinner
    private lateinit var btnSaveCharacter: Button

    private lateinit var personajeDAO: PersonajeDAO
    private lateinit var dndApiService: ApiService
    private var characterClasses = mutableListOf<Class>()
    private var characterRaces = mutableListOf<Race>()
    private var characterBackgrounds = mutableListOf<Background>()
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

        inputNombrePersonaje = findViewById(R.id.inputNombre)
        etLevel = findViewById(R.id.inputLevel)
        spinnerClass = findViewById(R.id.spClasses)
        spinnerRace = findViewById(R.id.spRaces)
        spinnerBackground = findViewById(R.id.spBackground)
        btnSaveCharacter = findViewById(R.id.btnGuardarPersonaje)

        personajeDAO = PersonajeDAO(this)

        initApi()
        fetchClasses()
        fetchRaces()
        fetchBackgrounds()

        btnSaveCharacter.setOnClickListener {
            val characterName = inputNombrePersonaje.text.toString()
            if (characterName.isEmpty()){
                Toast.makeText(this, "Ponle nombre a tu personaje", Toast.LENGTH_SHORT).show()
            }else {
                val selectedClassName = spinnerClass.selectedItem.toString()
                val selectedRaceName = spinnerRace.selectedItem.toString()
                val selectedBackgroundName = spinnerBackground.selectedItem.toString()

                val selectedClass = characterClasses.find { it.name == selectedClassName }
                val selectedRace = characterRaces.find { it.name == selectedRaceName }
                val selectedBackground = characterBackgrounds.find { it.name == selectedBackgroundName }

                val selectedLevel = etLevel.text.toString().toIntOrNull() ?: 1

                val newCharacter = selectedClass?.let { it1 ->
                    Personaje(
                        name = characterName,
                        charClass = it1.name,
                        level = selectedLevel,
                        race = selectedRace?.name ?: "",
                        classUrl = it1.url,
                        raceUrl = selectedRace?.url ?: "",
                        background = selectedBackground?.name ?: "",
                        backgroundUrl = selectedBackground?.url ?: "",
                        hitDie = 0,
                        userId = usuario!!
                    )
                }


                if (newCharacter != null) {
                    personajeDAO.insertPersonaje(newCharacter)
                    Toast.makeText(this, "Personaje creado", Toast.LENGTH_SHORT).show()
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

        val floatingBack: FloatingActionButton = findViewById(R.id.floatingBack)
        floatingBack.setOnClickListener {
            finish()
        }

    }

    private fun initApi(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dndApiService = retrofit.create(ApiService::class.java)
    }



    private fun fetchClasses() {


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getClasses()
                if (response.isSuccessful) {
                    val classesList = response.body()?.results

                    runOnUiThread {

                        classesList?.let {
                            characterClasses.clear()
                            characterClasses.addAll(it)


                            val classNames = it.map { it.name }

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





    private fun fetchRaces() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getRaces()
                if (response.isSuccessful) {
                    val racesList = response.body()?.results
                    runOnUiThread {
                        racesList?.let {
                            characterRaces.clear()
                            characterRaces.addAll(it)

                            val classNames = it.map { it.name }

                            val classAdapter = ArrayAdapter(this@CrearPersonajeActivity, android.R.layout.simple_spinner_item, classNames)
                            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerRace.adapter = classAdapter
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchBackgrounds() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = dndApiService.getBackgrounds()
                if (response.isSuccessful) {
                    val racesList = response.body()?.results
                    runOnUiThread {
                        racesList?.let {
                            characterBackgrounds.clear()
                            characterBackgrounds.addAll(it)

                            val backgroundsNames = it.map { it.name }

                            val classAdapter = ArrayAdapter(this@CrearPersonajeActivity, android.R.layout.simple_spinner_item, backgroundsNames)
                            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerBackground.adapter = classAdapter
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
