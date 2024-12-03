package com.example.dndcharacterbuilder.personajes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharacterbuilder2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PersonajesActivity : AppCompatActivity() {

    private lateinit var characterContainer: LinearLayout
    private lateinit var personajeDAO: PersonajeDAO
    private var usuario: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personajes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var bundle = intent.extras
        usuario = bundle?.getString("user")


        personajeDAO = PersonajeDAO(this)

        characterContainer = findViewById(R.id.characterContainer)

        //insertTestData()

        loadCharacters()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, CrearPersonajeActivity::class.java).apply {
                putExtra("user", usuario)
            }
            startActivity(intent)
        }
    }

    private fun insertTestData() {
        val personajes = listOf(
            Personaje(
                name = "Thorin",
                charClass = "Guerrero",
                level = 5,
                race = "Enano",
                userId = usuario!!,  // Debes asignar un ID de usuario válido
                classUrl = "/api/classes/guerrero",  // URL de ejemplo para la clase
                raceUrl = "/api/races/enano",  // URL de ejemplo para la raza
                background = "Un enano fuerte y valiente",
                backgroundUrl = "/api/backgrounds/enano",  // URL de ejemplo para el fondo
                hitDie = 12  // Valor de ejemplo para el dado de golpe
            ),
            Personaje(
                name = "Arwen",
                charClass = "Hechicera",
                level = 4,
                race = "Elfa",
                userId = usuario!!,  // Debes asignar un ID de usuario válido
                classUrl = "/api/classes/hechicera",  // URL de ejemplo para la clase
                raceUrl = "/api/races/elfa",  // URL de ejemplo para la raza
                background = "Una hechicera con magia ancestral",
                backgroundUrl = "/api/backgrounds/elfa",  // URL de ejemplo para el fondo
                hitDie = 6  // Valor de ejemplo para el dado de golpe
            ),
            Personaje(
                name = "Gimli",
                charClass = "Guerrero",
                level = 2,
                race = "Enano",
                userId = usuario!!,  // Debes asignar un ID de usuario válido
                classUrl = "/api/classes/guerrero",  // URL de ejemplo para la clase
                raceUrl = "/api/races/enano",  // URL de ejemplo para la raza
                background = "Enano con gran destreza en la batalla",
                backgroundUrl = "/api/backgrounds/enano",  // URL de ejemplo para el fondo
                hitDie = 12  // Valor de ejemplo para el dado de golpe
            ),
            Personaje(
                name = "Legolas",
                charClass = "Arquero",
                level = 4,
                race = "Elfo",
                userId = usuario!!,  // Debes asignar un ID de usuario válido
                classUrl = "/api/classes/arquero",  // URL de ejemplo para la clase
                raceUrl = "/api/races/elfo",  // URL de ejemplo para la raza
                background = "Un elfo experto en el uso del arco",
                backgroundUrl = "/api/backgrounds/elfo",  // URL de ejemplo para el fondo
                hitDie = 8  // Valor de ejemplo para el dado de golpe
            )
        )
        for (character in personajes) {
            personajeDAO.insertCharacter(character)
        }
    }


    private fun loadCharacters() {
        val characters = personajeDAO.getCharactersByUser(usuario!!)
        for (character in characters) {
            addCharacterView(character)
        }
    }

    private fun addCharacterView(personaje: Personaje) {
        // Inflar el diseño de cada tarjeta
        val inflater = LayoutInflater.from(this)
        val characterView = inflater.inflate(R.layout.personaje_item, characterContainer, false)

        // Asignar los valores del personaje a la vista
        val tvCharacterName = characterView.findViewById<TextView>(R.id.tvCharacterName)
        val tvCharacterClassAndLevel =
            characterView.findViewById<TextView>(R.id.tvCharacterClassAndLevel)

        tvCharacterName.text = personaje.name
        tvCharacterClassAndLevel.text = "${personaje.charClass} - Nivel ${personaje.level}"

        // Añadir la vista de personaje al contenedor
        characterContainer.addView(characterView)
    }
}