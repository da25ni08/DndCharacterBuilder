package com.example.dndcharacterbuilder

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dndcharacterbuilder.personajes.AdminSQLiteOpenHelper
import com.example.dndcharacterbuilder2.R

class MainActivity : AppCompatActivity() {

    private lateinit var botonSingin: Button
    private lateinit var botonLogin: Button
    private lateinit var inputUsername: EditText
    private lateinit var inputPassword: EditText

    private lateinit var admin: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        botonLogin = findViewById(R.id.btnLogin)
        botonSingin = findViewById(R.id.btnSingin)

        inputPassword = findViewById(R.id.inputPassword)
        inputUsername = findViewById(R.id.inputUsername)

        admin = AdminSQLiteOpenHelper(this,  null)
    }

    fun logIn(v: View) {
        val acciones: (SQLiteDatabase, String) -> String = { _, password ->
            val resultado: String
            if (password == inputPassword.text.toString()) {
                val username = inputUsername.text.toString()

                val intento = Intent(this, InicioActivity::class.java).apply {
                    putExtra("user", username)
                }
                startActivity(intento)
                resultado = "Bienvenido"
            } else {
                resultado = "Contraseña incorrecta"
            }
            resultado
        }
        checkUser(acciones)
    }

    fun singUp(v: View) {
        val intento = Intent(this, SingUp::class.java)
        startActivity(intento)
    }

    private fun checkUser(acciones: (SQLiteDatabase, String) -> String) {
        val db = admin.writableDatabase
        val fila = db.rawQuery(
            "SELECT ${AdminSQLiteOpenHelper.COLUMN_USERS_PASSWD} FROM ${AdminSQLiteOpenHelper.TABLE_USERS} WHERE ${AdminSQLiteOpenHelper.COLUMN_USERS_USER} = '${inputUsername.text.toString()}'",
            null
        )

        if (fila.moveToFirst()) {
            val password = fila.getString(0)
            val resultado = acciones(db, password)
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Usuario inexistente", Toast.LENGTH_SHORT).show()
        }
        fila.close()
        db.close()
    }
}