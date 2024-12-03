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
    val userId: String,  // Aseguramos que el personaje tenga asignado un userId
    val classUrl: String,  // URL de la clase (de la API)
    val raceUrl: String,  // URL de la raza (de la API)
    val background: String,  // Descripción del fondo del personaje
    val backgroundUrl: String,  // URL del fondo del personaje
    val hitDie: Int // Valor del dado de golpe (ej. 12 para un Guerrero)
)


class PersonajeDAO(context: Context) {

    private val dbHelper = AdminSQLiteOpenHelper(
        context,
        null,
    )

    // Insertar un personaje en la base de datos, con un userId asociado
    fun insertCharacter(personaje: Personaje): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID, personaje.userId) // Guardamos el userId
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

    // Obtener todos los personajes de un usuario específico (filtrado por userId)
    fun getCharactersByUser(userId: String): List<Personaje> {
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
            arrayOf(userId), // Filtro por userId
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
                    userId = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID)), // Leer userId
                    classUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_URL)), // Leer classUrl
                    raceUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_URL)), // Leer raceUrl
                    background = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND)), // Leer background
                    backgroundUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND_URL)), // Leer backgroundUrl
                    hitDie = cursor.getInt(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_HIT_DIE)) // Leer hitDie
                )
                personajeList.add(personaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return personajeList
    }

    // Obtener un personaje por su ID, filtrado por userId
    fun getCharacterById(characterId: Long, userId: String): Personaje? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
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
            "${AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID} = ? AND ${AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID} = ?",
            arrayOf(characterId.toString(), userId),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            Personaje(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_NAME)),
                charClass = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_NAME)),
                level = cursor.getInt(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_LEVEL)),
                race = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_NAME)),
                userId = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID)), // Leer userId
                classUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_CLASS_URL)), // Leer classUrl
                raceUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_RACE_URL)), // Leer raceUrl
                background = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND)), // Leer background
                backgroundUrl = cursor.getString(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_BACKGROUND_URL)), // Leer backgroundUrl
                hitDie = cursor.getInt(cursor.getColumnIndexOrThrow(AdminSQLiteOpenHelper.COLUMN_CHARACTER_HIT_DIE)) // Leer hitDie
            )
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    // Actualizar los datos de un personaje
    fun updateCharacter(personaje: Personaje): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
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
        return db.update(
            AdminSQLiteOpenHelper.TABLE_CHARACTERS,
            values,
            "${AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID} = ? AND ${AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID} = ?",
            arrayOf(personaje.id.toString(), personaje.userId)
        )
    }

    // Eliminar un personaje
    fun deleteCharacter(characterId: Long, userId: String): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AdminSQLiteOpenHelper.TABLE_CHARACTERS,
            "${AdminSQLiteOpenHelper.COLUMN_CHARACTER_ID} = ? AND ${AdminSQLiteOpenHelper.COLUMN_CHARACTER_USER_ID} = ?",
            arrayOf(characterId.toString(), userId)
        )
    }
}
