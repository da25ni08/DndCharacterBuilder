package com.example.dndcharacterbuilder.personajes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


private const val DATABASE_NAME = "DnDCharacterDB"
private const val DATABASE_VERSION = 3

class AdminSQLiteOpenHelper (
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {


        //Tabla de usuarios
        const val TABLE_USERS = "Users"
        const val COLUMN_USERS_USER = "user"
        const val COLUMN_USERS_PASSWD = "password"

        // Tabla de personajes
        const val TABLE_CHARACTERS = "Characters"
        const val COLUMN_CHARACTER_USER_ID = "user"
        const val COLUMN_CHARACTER_ID = "id"
        const val COLUMN_CHARACTER_NAME = "name"
        const val COLUMN_CHARACTER_CLASS_NAME = "class"
        const val COLUMN_CHARACTER_CLASS_URL = "class_url"
        const val COLUMN_CHARACTER_RACE_NAME = "race"
        const val COLUMN_CHARACTER_RACE_URL = "race_url"
        const val COLUMN_CHARACTER_BACKGROUND = "background"
        const val COLUMN_CHARACTER_BACKGROUND_URL = "background_url"
        const val COLUMN_CHARACTER_LEVEL = "level"
        const val COLUMN_CHARACTER_HIT_DIE = "hit_die"

        // Tabla de clases y niveles
        const val TABLE_CLASS_LEVELS = "ClassLevels"
        const val COLUMN_CLASS_LEVEL_CHARACTER_ID = "character_id"
        const val COLUMN_CLASS_NAME = "class_name"
        const val COLUMN_LEVEL = "level"
        const val COLUMN_HIT_DIE = "hit_die"
        const val COLUMN_SPELL_SLOTS_LEVEL_1 = "spell_slots_level_1"
        const val COLUMN_SPELL_SLOTS_LEVEL_2 = "spell_slots_level_2"
        const val COLUMN_SPELL_SLOTS_LEVEL_3 = "spell_slots_level_3"
        const val COLUMN_SPELL_SLOTS_LEVEL_4 = "spell_slots_level_4"
        const val COLUMN_SPELL_SLOTS_LEVEL_5 = "spell_slots_level_5"
        const val COLUMN_SPELL_SLOTS_LEVEL_6 = "spell_slots_level_6"
        const val COLUMN_SPELL_SLOTS_LEVEL_7 = "spell_slots_level_7"
        const val COLUMN_SPELL_SLOTS_LEVEL_8 = "spell_slots_level_8"
        const val COLUMN_SPELL_SLOTS_LEVEL_9 = "spell_slots_level_9"
        const val COLUMN_SUBCLASS_NAME = "subclass_name"
        const val COLUMN_SUBCLASS_URL = "subclass_url"

        // Tabla de proficiencias
        const val TABLE_PROFICIENCIES = "Proficiencies"
        const val COLUMN_PROFICIENCY_NAME = "proficiency_name"
        const val COLUMN_PROFICIENCY_TYPE = "proficiency_type"
        const val COLUMN_PROFICIENCY_CHARACTER_ID = "character_id"
        const val COLUMN_PROFICIENCY_URL = "proficiency_url"

        // Tabla de habilidades (Ability Scores)
        const val TABLE_ABILITY_SCORES = "AbilityScores"
        const val TABLE_ABILITY_CHARACTER_ID = "character_id"
        const val COLUMN_ABILITY_NAME = "ability_name"
        const val COLUMN_ABILITY_VALUE = "ability_value"

        // Tabla de equipamiento
        const val TABLE_EQUIPMENT = "Equipment"
        const val TABLE_EQUIPMENT_CHARACTER_ID = "character_id"
        const val COLUMN_EQUIPMENT_NAME = "equipment_name"
        const val COLUMN_EQUIPMENT_URL = "url"
        const val COLUMN_EQUIPMENT_QUANTITY = "quantity"

        // Tabla de hechizos
        const val TABLE_SPELLS = "Spells"
        const val TABLE_SPELLS_CHARACTER_ID = "character_id"
        const val COLUMN_SPELL_NAME = "spell_name"
        const val COLUMN_SPELL_URL = "url"

        // Tabla de feats
        const val TABLE_FEATS = "Feats"
        const val COLUMN_FEATS_CHARACTER_ID = "character_id"
        const val COLUMN_FEATS_NAME = "name"
        const val COLUMN_FEATS_URL = "url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        //Crear tabla de usuarios
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USERS_USER TEXT NOT NULL PRIMARY KEY,
                $COLUMN_USERS_PASSWD TEXT NOT NULL
            )
            """.trimIndent()
        db.execSQL(createUsersTable)

        // Crear tabla de personajes
        val createCharactersTable = """
            CREATE TABLE $TABLE_CHARACTERS (
                $COLUMN_CHARACTER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CHARACTER_USER_ID TEXT NOT NULL,
                $COLUMN_CHARACTER_NAME TEXT NOT NULL,
                $COLUMN_CHARACTER_CLASS_NAME TEXT,
                $COLUMN_CHARACTER_CLASS_URL TEXT,
                $COLUMN_CHARACTER_RACE_NAME TEXT,
                $COLUMN_CHARACTER_RACE_URL TEXT,
                $COLUMN_CHARACTER_BACKGROUND TEXT,
                $COLUMN_CHARACTER_BACKGROUND_URL TEXT,
                $COLUMN_CHARACTER_LEVEL INTEGER,
                $COLUMN_CHARACTER_HIT_DIE INTEGER,
                FOREIGN KEY ($COLUMN_CHARACTER_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USERS_USER)
            )
        """.trimIndent()
        db.execSQL(createCharactersTable)

        // Crear tabla combinada de clases y niveles
        val createClassLevelsTable = """
            CREATE TABLE $TABLE_CLASS_LEVELS (
                $COLUMN_CLASS_LEVEL_CHARACTER_ID INTEGER,
                $COLUMN_CLASS_NAME TEXT,
                $COLUMN_LEVEL INTEGER,
                $COLUMN_HIT_DIE INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_1 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_2 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_3 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_4 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_5 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_6 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_7 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_8 INTEGER,
                $COLUMN_SPELL_SLOTS_LEVEL_9 INTEGER,
                $COLUMN_SUBCLASS_NAME TEXT,
                $COLUMN_SUBCLASS_URL TEXT,
                PRIMARY KEY ($COLUMN_CLASS_LEVEL_CHARACTER_ID, $COLUMN_CLASS_NAME, $COLUMN_LEVEL),
                FOREIGN KEY ($COLUMN_CLASS_LEVEL_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createClassLevelsTable)

        // Crear tabla de proficiencias
        val createProficienciesTable = """
            CREATE TABLE $TABLE_PROFICIENCIES (
                $COLUMN_PROFICIENCY_NAME TEXT,
                $COLUMN_PROFICIENCY_TYPE TEXT,
                $COLUMN_PROFICIENCY_URL TEXT,
                $COLUMN_PROFICIENCY_CHARACTER_ID INTEGER,
                PRIMARY KEY ($COLUMN_PROFICIENCY_CHARACTER_ID, $COLUMN_PROFICIENCY_NAME),
                FOREIGN KEY ($COLUMN_PROFICIENCY_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createProficienciesTable)

        // Crear tabla de habilidades (Ability Scores)
        val createAbilityScoresTable = """
            CREATE TABLE $TABLE_ABILITY_SCORES (
                $COLUMN_ABILITY_NAME TEXT,
                $COLUMN_ABILITY_VALUE INTEGER,
                $TABLE_ABILITY_CHARACTER_ID INTEGER,
                PRIMARY KEY ($TABLE_ABILITY_CHARACTER_ID, $COLUMN_ABILITY_NAME),
                FOREIGN KEY ($TABLE_ABILITY_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createAbilityScoresTable)

        // Crear tabla de equipamiento
        val createEquipmentTable = """
            CREATE TABLE $TABLE_EQUIPMENT (
                $COLUMN_EQUIPMENT_NAME TEXT,
                $COLUMN_EQUIPMENT_URL TEXT,
                $COLUMN_EQUIPMENT_QUANTITY INTEGER,
                $TABLE_EQUIPMENT_CHARACTER_ID INTEGER,
                PRIMARY KEY ($TABLE_EQUIPMENT_CHARACTER_ID, $COLUMN_EQUIPMENT_NAME),
                FOREIGN KEY ($TABLE_EQUIPMENT_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createEquipmentTable)

        // Crear tabla de hechizos
        val createSpellsTable = """
            CREATE TABLE $TABLE_SPELLS (
                $COLUMN_SPELL_NAME TEXT,
                $COLUMN_SPELL_URL TEXT,
                $TABLE_SPELLS_CHARACTER_ID INTEGER,
                PRIMARY KEY ($TABLE_SPELLS_CHARACTER_ID, $COLUMN_SPELL_NAME),
                FOREIGN KEY ($TABLE_SPELLS_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createSpellsTable)

        // Crear tabla de feats
        val createFeatsTable = """
            CREATE TABLE $TABLE_FEATS (
                $COLUMN_FEATS_NAME TEXT,
                $COLUMN_FEATS_URL TEXT,
                $COLUMN_FEATS_CHARACTER_ID INTEGER,
                PRIMARY KEY ($COLUMN_FEATS_CHARACTER_ID, $COLUMN_FEATS_NAME),
                FOREIGN KEY ($COLUMN_FEATS_CHARACTER_ID) REFERENCES $TABLE_CHARACTERS($COLUMN_CHARACTER_ID)
            )
        """.trimIndent()
        db.execSQL(createFeatsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFICIENCIES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_FEATS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_SPELLS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_EQUIPMENT")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ABILITY_SCORES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CLASS_LEVELS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTERS")
            onCreate(db)
        }
    }

}
