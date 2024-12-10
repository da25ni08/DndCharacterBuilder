package com.example.dndcharacterbuilder.personajes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class Personaje(
    val id: Long = 0,
    val name: String,
    val charClass: String,
    val level: Int,
    val race: String,
    val userId: String,
    val classUrl: String,
    val raceUrl: String,
    val background: String,
    val backgroundUrl: String,
    val hitDie: Int
)


class PersonajeDAO(context: Context) {

    private val dbHelper = AdminSQLiteOpenHelper(
        context,
        null,
    )

    fun insertPersonaje(personaje: Personaje): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID, personaje.userId)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_NAME, personaje.name)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_NAME, personaje.charClass)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_URL, personaje.classUrl)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_NAME, personaje.race)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_URL, personaje.raceUrl)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND, personaje.background)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND_URL, personaje.backgroundUrl)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_LEVEL, personaje.level)
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_HIT_DIE, personaje.hitDie)
        }
        return db.insert(AdminSQLiteOpenHelper.TABLE_CHARACTERS, null, values)
    }

    fun getPersonajeByUsuario(userId: String): List<Personaje> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AdminSQLiteOpenHelper.TABLE_CHARACTERS,
            arrayOf(
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_NAME,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_NAME,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_URL,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_NAME,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_URL,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND_URL,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_LEVEL,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_HIT_DIE,
                AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID
            ),
            "${AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID} = ?",
            arrayOf(userId),
            null, null, "name ASC"
        )

        val personajeList = mutableListOf<Personaje>()
        if (cursor.moveToFirst()) {
            do {
                val personaje = Personaje(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_NAME)),
                    charClass = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_NAME)),
                    level = cursor.getInt(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_LEVEL)),
                    race = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_NAME)),
                    userId = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID)),
                    classUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_URL)),
                    raceUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_URL)),
                    background = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND)),
                    backgroundUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND_URL)),
                    hitDie = cursor.getInt(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_HIT_DIE))
                )
                personajeList.add(personaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return personajeList
    }


}
