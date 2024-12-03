package com.example.dndcharacterbuilder

import android.content.ContentValues
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

        botonLogin = findViewById<Button>(R.id.buttonLogin)
        botonSingin = findViewById<Button>(R.id.buttonSingin)

        inputPassword = findViewById<EditText>(R.id.editTextPassword)
        inputUsername = findViewById<EditText>(R.id.editTextUsername)

        admin = AdminSQLiteOpenHelper(this,  null)
    }

    fun logIn(v: View) {
        val acciones: (SQLiteDatabase, String) -> String = { db, password ->
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

    fun singIn(v: View) {
        val newPassword = inputPassword.text.toString()
        val db = admin.writableDatabase
        val fila = db.rawQuery(
            "SELECT ${AdminSQLiteOpenHelper.COLUMN_USERS_USER} FROM ${AdminSQLiteOpenHelper.TABLE_USERS} WHERE ${AdminSQLiteOpenHelper.COLUMN_USERS_USER} = '${inputUsername.text.toString()}'",
            null
        )

        if (fila.moveToFirst()) {
            Toast.makeText(this, "Error al crear los datos: Usuario ya existe", Toast.LENGTH_SHORT)
                .show()
        } else {
            val passwordSecurity = checkPasswordSecurity(newPassword)
            if (passwordSecurity != null) {
                Toast.makeText(this, passwordSecurity, Toast.LENGTH_SHORT).show()
                return
            }

            val registro = ContentValues().apply {
                put(AdminSQLiteOpenHelper.COLUMN_USERS_USER, inputUsername.text.toString())
                put(AdminSQLiteOpenHelper.COLUMN_USERS_PASSWD, newPassword)
            }

            db.insert(AdminSQLiteOpenHelper.TABLE_USERS, null, registro)
            Toast.makeText(this, "Datos creados con éxito", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        }
        fila.close()
        db.close()
    }

    private fun limpiarCampos() {
        inputPassword.setText("")
        inputUsername.setText("")
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