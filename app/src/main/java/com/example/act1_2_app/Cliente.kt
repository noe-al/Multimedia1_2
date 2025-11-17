package com.example.act1_2_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cliente(val id: Long = 0, val nombre: String, val correo: String, val telefono: String) : Parcelable