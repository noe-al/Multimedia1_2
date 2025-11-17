package com.example.act1_2_app

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns

class ClienteDataSource(context: Context) {

    private val dbHelper = ClienteDbHelper(context)

    fun getAllClientes(): MutableList<Cliente> {
        return searchClientes("")
    }

    fun searchClientes(query: String): MutableList<Cliente> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE, ClienteContract.ClienteEntry.COLUMN_NAME_CORREO, ClienteContract.ClienteEntry.COLUMN_NAME_TELEFONO)

        val selection = "${ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE} LIKE ?"
        val selectionArgs = arrayOf("$query%")

        val cursor = db.query(
            ClienteContract.ClienteEntry.TABLE_NAME,
            projection,
            if (query.isNotEmpty()) selection else null,
            if (query.isNotEmpty()) selectionArgs else null,
            null,
            null,
            null
        )

        val clientes = mutableListOf<Cliente>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val nombre = getString(getColumnIndexOrThrow(ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE))
                val correo = getString(getColumnIndexOrThrow(ClienteContract.ClienteEntry.COLUMN_NAME_CORREO))
                val telefono = getString(getColumnIndexOrThrow(ClienteContract.ClienteEntry.COLUMN_NAME_TELEFONO))
                clientes.add(Cliente(id, nombre, correo, telefono))
            }
        }
        cursor.close()
        db.close()
        return clientes
    }

    fun addCliente(cliente: Cliente): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE, cliente.nombre)
            put(ClienteContract.ClienteEntry.COLUMN_NAME_CORREO, cliente.correo)
            put(ClienteContract.ClienteEntry.COLUMN_NAME_TELEFONO, cliente.telefono)
        }
        val newRowId = db.insert(ClienteContract.ClienteEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun updateCliente(cliente: Cliente): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ClienteContract.ClienteEntry.COLUMN_NAME_NOMBRE, cliente.nombre)
            put(ClienteContract.ClienteEntry.COLUMN_NAME_CORREO, cliente.correo)
            put(ClienteContract.ClienteEntry.COLUMN_NAME_TELEFONO, cliente.telefono)
        }
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(cliente.id.toString())
        val count = db.update(
            ClienteContract.ClienteEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        db.close()
        return count
    }

    fun deleteCliente(id: Long): Int {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(ClienteContract.ClienteEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRows
    }
}