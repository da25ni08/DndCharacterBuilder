package com.example.dndcharacterbuilder

import android.content.ContentValues
import android.content.Intent
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

class SingUp : AppCompatActivity() {

    private lateinit var admin: AdminSQLiteOpenHelper

    private lateinit var botonSingin: Button
    private lateinit var botonLogin: Button
    private lateinit var inputUsuario: EditText
    private lateinit var inputPasswd: EditText
    private lateinit var inputRepPasswd: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sing_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        botonLogin = findViewById(R.id.btnLogin)
        botonSingin = findViewById(R.id.btnSingin)
        inputPasswd = findViewById(R.id.inputPassword)
        inputUsuario = findViewById(R.id.inputUsername)
        inputRepPasswd = findViewById(R.id.editTextRepeatPassword)

        admin = AdminSQLiteOpenHelper(this,  null)


    }

    fun singUp(view: android.view.View) {
        val username = inputUsuario.text.toString()
        val password = inputPasswd.text.toString()
        val repeatPassword = inputRepPasswd.text.toString()

        if (password != repeatPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }


        if (createUser(username, password)){
            val intento = Intent(this, InicioActivity::class.java).apply {
                putExtra("user", username)
            }
            startActivity(intento)
        }


    }


    private fun createUser(username: String, password:String): Boolean {
        val db = admin.writableDatabase
        val fila = db.rawQuery(
            "SELECT ${AdminSQLiteOpenHelper.COLUMN_USERS_USER} FROM ${AdminSQLiteOpenHelper.TABLE_USERS} WHERE ${AdminSQLiteOpenHelper.COLUMN_USERS_USER} = '${username}'",
            null
        )

        if (fila.moveToFirst()) {
            Toast.makeText(this, "Error al crear los datos: Usuario ya existe", Toast.LENGTH_SHORT)
                .show()
            fila.close()
            db.close()
            return false
        } else {
            val passwordSecurity = checkPasswordSecurity(password)
            if (passwordSecurity != null) {
                Toast.makeText(this, passwordSecurity, Toast.LENGTH_SHORT).show()
                return false
            }

            val registro = ContentValues().apply {
                put(AdminSQLiteOpenHelper.COLUMN_USERS_USER, username)
                put(AdminSQLiteOpenHelper.COLUMN_USERS_PASSWD, password)
            }

            db.insert(AdminSQLiteOpenHelper.TABLE_USERS, null, registro)
        }
        fila.close()
        db.close()
        Toast.makeText(this, "Usuario creado con éxito, bienvenido", Toast.LENGTH_SHORT).show()
        return true
    }

    fun logIn(v: View) {
        val intento = Intent(this, MainActivity::class.java)
        startActivity(intento)
    }



    private fun checkPasswordSecurity(password: String): String? {
        val patternMayusculas = "[A-Z]+".toRegex()
        val patternLetras = "\\D+".toRegex()
        val patternNumeros = "\\d+".toRegex()
        var result: String? = null

        if (password.length < 8) {
            result = "Muy corta"
        }
        if (!patternNumeros.containsMatchIn(password)) {
            result = if (result == null) "Faltan números" else "$result | Faltan números"
        }
        if (!patternLetras.containsMatchIn(password)) {
            result = if (result == null) "Faltan letras" else "$result | Faltan letras"
        }
        if (!patternMayusculas.containsMatchIn(password)) {
            result = if (result == null) "Faltan mayúsculas" else "$result | Faltan mayúsculas"
        }
        return result
    }



}