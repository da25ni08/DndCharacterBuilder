package com.example.dndcharachterbuilder.personajes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.dndcharacterbuilder.personajes.AdminSQLiteOpenHelper

data class Personaje(
    val id: Long = 0,
    val name: String,
    val charClass: String,
    val level: Int,
    val race: String
)

class PersonajeDAO(context: Context) {

    private val dbHelper = AdminSQLiteOpenHelper(
        context,
        "DnDCharacterDB",
        null,
        1
    )

    fun insertCharacter(personaje: Personaje): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", personaje.name)
            put("class", personaje.charClass)
            put("level", personaje.level)
            put("race", personaje.race)
        }
        return db.insert("Characters", null, values)
    }

    // Obtener todos los personajes de la base de datos
    fun getAllCharacters(): List<Personaje> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "Characters",
            arrayOf("id", "name", "class", "level", "race"),
            null, null, null, null, "name ASC"
        )

        val personajeList = mutableListOf<Personaje>()
        if (cursor.moveToFirst()) {
            do {
                val personaje = Personaje(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    charClass = cursor.getString(cursor.getColumnIndexOrThrow("class")),
                    level = cursor.getInt(cursor.getColumnIndexOrThrow("level")),
                    race = cursor.getString(cursor.getColumnIndexOrThrow("race"))
                )
                personajeList.add(personaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return personajeList
    }

    // Obtener un personaje por su ID
    fun getCharacterById(characterId: Long): Personaje? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "Characters",
            arrayOf("id", "name", "class", "level", "race"),
            "id = ?",
            arrayOf(characterId.toString()),
            null,
            null,
            null
        )

        val personaje = if (cursor.moveToFirst()) {
            Personaje(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                charClass = cursor.getString(cursor.getColumnIndexOrThrow("class")),
                level = cursor.getInt(cursor.getColumnIndexOrThrow("level")),
                race = cursor.getString(cursor.getColumnIndexOrThrow("race"))
            )
        } else {
            null
        }

        cursor.close()
        return personaje
    }

    // Actualizar los datos de un personaje
    fun updateCharacter(personaje: Personaje): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", personaje.name)
            put("class", personaje.charClass)
            put("level", personaje.level)
            put("race", personaje.race)
        }
        return db.update(
            "Characters",
            values,
            "id = ?",
            arrayOf(personaje.id.toString())
        )
    }

    // Eliminar un personaje
    fun deleteCharacter(characterId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            "Characters",
            "id = ?",
            arrayOf(characterId.toString())
        )
    }
}