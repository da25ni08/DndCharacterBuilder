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
import com.example.dndcharachterbuilder.personajes.Personaje
import com.example.dndcharachterbuilder.personajes.PersonajeDAO
import com.example.dndcharacterbuilder2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Personajes : AppCompatActivity() {

    private lateinit var characterContainer: LinearLayout
    private lateinit var personajeDAO: PersonajeDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_personajes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        personajeDAO = PersonajeDAO(this)

        characterContainer = findViewById(R.id.characterContainer)

        //insertTestData()

        loadCharacters()

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, CrearPersonaje::class.java)
            startActivity(intent)
        }
    }

    private fun insertTestData() {
        val personajes = listOf(
            Personaje(name = "Thorin", charClass = "Guerrero", level = 5, race = "Enano"),
            Personaje(name = "Arwen", charClass = "Hechicera", level = 4, race = "Elfa"),
            Personaje(name = "Gimli", charClass = "Guerrero", level = 2, race = "Enano"),
            Personaje(name = "Legolas", charClass = "Arquero", level = 4, race = "Elfo")
        )

        for (character in personajes) {
            personajeDAO.insertCharacter(character)
        }
    }


    private fun loadCharacters() {
        val characters = personajeDAO.getAllCharacters()
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
        val tvCharacterClassAndLevel = characterView.findViewById<TextView>(R.id.tvCharacterClassAndLevel)

        tvCharacterName.text = personaje.name
        tvCharacterClassAndLevel.text = "${personaje.charClass} - Nivel ${personaje.level}"

        // Añadir la vista de personaje al contenedor
        characterContainer.addView(characterView)
    }
}