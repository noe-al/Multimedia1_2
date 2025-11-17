package com.example.act1_2_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var clienteAdapter: ClienteAdapter
    private lateinit var dataSource: ClienteDataSource
    private var clientes = mutableListOf<Cliente>()
    private lateinit var totalClientesTextView: TextView

    private val addClienteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val nombre = data?.getStringExtra(AddEditClienteActivity.EXTRA_NOMBRE) ?: ""
            val correo = data?.getStringExtra(AddEditClienteActivity.EXTRA_CORREO) ?: ""
            val telefono = data?.getStringExtra(AddEditClienteActivity.EXTRA_TELEFONO) ?: ""
            val nuevoCliente = Cliente(nombre = nombre, correo = correo, telefono = telefono)
            dataSource.addCliente(nuevoCliente)
            refreshClientes()
        }
    }

    private val editClienteResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val id = data?.getLongExtra(AddEditClienteActivity.EXTRA_ID, 0) ?: 0
            if (id != 0L) {
                val nombre = data?.getStringExtra(AddEditClienteActivity.EXTRA_NOMBRE) ?: ""
                val correo = data?.getStringExtra(AddEditClienteActivity.EXTRA_CORREO) ?: ""
                val telefono = data?.getStringExtra(AddEditClienteActivity.EXTRA_TELEFONO) ?: ""
                val clienteActualizado = Cliente(id, nombre, correo, telefono)
                dataSource.updateCliente(clienteActualizado)
                refreshClientes()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataSource = ClienteDataSource(this)

        totalClientesTextView = findViewById(R.id.totalClientesTextView)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        clienteAdapter = ClienteAdapter(clientes)
        recyclerView.adapter = clienteAdapter

        refreshClientes()

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                refreshClientes(newText.orEmpty())
                return true
            }
        })

        clienteAdapter.onEditClick = { cliente ->
            val intent = Intent(this, AddEditClienteActivity::class.java)
            intent.putExtra(AddEditClienteActivity.EXTRA_CLIENTE, cliente)
            editClienteResultLauncher.launch(intent)
        }

        clienteAdapter.onDeleteClick = { cliente ->
            AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar a ${cliente.nombre}?\n\nCorreo: ${cliente.correo}\nTeléfono: ${cliente.telefono}")
                .setPositiveButton("Sí") { _, _ ->
                    dataSource.deleteCliente(cliente.id)
                    refreshClientes()
                }
                .setNegativeButton("No", null)
                .show()
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, AddEditClienteActivity::class.java)
            addClienteResultLauncher.launch(intent)
        }
    }

    private fun refreshClientes(query: String = "") {
        clientes.clear()
        clientes.addAll(dataSource.searchClientes(query))
        clienteAdapter.notifyDataSetChanged()
        totalClientesTextView.text = "Total de clientes: ${clientes.size}"
    }
}