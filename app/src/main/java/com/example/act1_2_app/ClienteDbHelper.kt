package com.example.act1_2_app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object ClienteContract {
    object ClienteEntry : BaseColumns {
        const val TABLE_NAME = "clientes"
        const val COLUMN_NAME_NOMBRE = "nombre"
        const val COLUMN_NAME_CORREO = "correo"
        const val COLUMN_NAME_TELEFONO = "telefono"
    }
}

class ClienteDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Clientes.db"

        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${ClienteContract.ClienteEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE} TEXT," +
                "${ClienteContract.ClienteEntry.COLUMN_NAME_CORREO} TEXT," +
                "${ClienteContract.ClienteEntry.COLUMN_NAME_TELEFONO} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ClienteContract.ClienteEntry.TABLE_NAME}"
    }
}