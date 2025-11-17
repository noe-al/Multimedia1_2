package com.example.act1_2_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(private var clientes: MutableList<Cliente>) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    var onEditClick: ((Cliente) -> Unit)? = null
    var onDeleteClick: ((Cliente) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.nombreTextView.text = cliente.nombre
        holder.correoTextView.text = cliente.correo
        holder.telefonoTextView.text = cliente.telefono

        holder.editButton.setOnClickListener {
            onEditClick?.invoke(cliente)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick?.invoke(cliente)
        }
    }

    override fun getItemCount() = clientes.size

    fun updateClientes(newClientes: List<Cliente>) {
        clientes.clear()
        clientes.addAll(newClientes)
        notifyDataSetChanged()
    }

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val correoTextView: TextView = itemView.findViewById(R.id.correoTextView)
        val telefonoTextView: TextView = itemView.findViewById(R.id.telefonoTextView)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}