package com.example.act1_2_app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddEditClienteActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var telefonoEditText: EditText

    private var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_cliente)

        nombreEditText = findViewById(R.id.nombreEditText)
        correoEditText = findViewById(R.id.correoEditText)
        telefonoEditText = findViewById(R.id.telefonoEditText)

        if (intent.hasExtra(EXTRA_CLIENTE)) {
            cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_CLIENTE, Cliente::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_CLIENTE)
            }

            cliente?.let {
                nombreEditText.setText(it.nombre)
                correoEditText.setText(it.correo)
                telefonoEditText.setText(it.telefono)
            }
        }

        val guardarButton: Button = findViewById(R.id.guardarButton)
        guardarButton.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
        val nombre = nombreEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()

        if (nombre.isEmpty()) {
            nombreEditText.error = "Campo obligatorio"
            return
        }

        if (correo.isEmpty()) {
            correoEditText.error = "Campo obligatorio"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoEditText.error = "Formato de correo incorrecto"
            return
        }

        if (telefono.isEmpty()) {
            telefonoEditText.error = "Campo obligatorio"
            return
        }

        if (telefono.length < 9) {
            telefonoEditText.error = "El teléfono debe tener al menos 9 dígitos"
            return
        }

        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_NOMBRE, nombre)
        resultIntent.putExtra(EXTRA_CORREO, correo)
        resultIntent.putExtra(EXTRA_TELEFONO, telefono)
        cliente?.let {
            resultIntent.putExtra(EXTRA_ID, it.id)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val EXTRA_CLIENTE = "com.example.act1_2_app.EXTRA_CLIENTE"
        const val EXTRA_ID = "com.example.act1_2_app.EXTRA_ID"
        const val EXTRA_NOMBRE = "com.example.act1_2_app.EXTRA_NOMBRE"
        const val EXTRA_CORREO = "com.example.act1_2_app.EXTRA_CORREO"
        const val EXTRA_TELEFONO = "com.example.act1_2_app.EXTRA_TELEFONO"
    }
}