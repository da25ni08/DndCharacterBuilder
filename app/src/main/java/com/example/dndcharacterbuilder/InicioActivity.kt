package com.example.dndcharacterbuilder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharacterbuilder.biblioteca.BibliotecaActivity
import com.example.dndcharacterbuilder.personajes.PersonajesActivity
import com.example.dndcharacterbuilder2.R

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = intent.extras
        val usuario = bundle?.getString("user")

        findViewById<Button>(R.id.btnBiblioteca).setOnClickListener {
            val intent = Intent(this, BibliotecaActivity::class.java).apply {
                putExtra("user", usuario)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnPersonajes).setOnClickListener {
            val intent = Intent(this, PersonajesActivity::class.java).apply {
                putExtra("user", usuario)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            finish()
        }
    }
}
