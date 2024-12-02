package com.example.dndcharacterbuilder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharacterbuilder.biblioteca.Biblioteca
import com.example.dndcharacterbuilder.personajes.Personajes
import com.example.dndcharacterbuilder2.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnExploreData).setOnClickListener {
            val intent = Intent(this, Biblioteca::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnSavedCharacters).setOnClickListener {
            val intent = Intent(this, Personajes::class.java)
            startActivity(intent)
        }
    }
}