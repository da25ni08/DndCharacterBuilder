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

    private lateinit var personajesLayout: LinearLayout
    private lateinit var personajeDAO: PersonajeDAO
    private var usuario: String? = null
    private lateinit var header: TextView


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

        header = findViewById(R.id.header)

        header.text = "${header.text} $usuario"

        personajesLayout = findViewById(R.id.personajesLayout)

        cargarPersonajes()

        val floatingCrearPersonaje: FloatingActionButton = findViewById(R.id.floatinCrearPersonaje)
        floatingCrearPersonaje.setOnClickListener {
            val intent = Intent(this, CrearPersonajeActivity::class.java).apply {
                putExtra("user", usuario)
            }
            startActivity(intent)
        }

        val floatingBack: FloatingActionButton = findViewById(R.id.floatingBack)
        floatingBack.setOnClickListener {
            finish()
        }


    }


    private fun cargarPersonajes() {
        val characters = personajeDAO.getPersonajeByUsuario(usuario!!)
        for (character in characters) {
            addVistaPersonajes(character)
        }
    }

    private fun addVistaPersonajes(personaje: Personaje) {
        val inflater = LayoutInflater.from(this)
        val characterView = inflater.inflate(R.layout.personaje_item, personajesLayout, false)

        val tvCharacterName = characterView.findViewById<TextView>(R.id.tvCharacterName)
        val tvCharacterClassAndLevel =
            characterView.findViewById<TextView>(R.id.tvCharacterClassAndLevel)

        tvCharacterName.text = personaje.name
        tvCharacterClassAndLevel.text = "${personaje.charClass} - Nivel ${personaje.level}"

        personajesLayout.addView(characterView)
    }
}