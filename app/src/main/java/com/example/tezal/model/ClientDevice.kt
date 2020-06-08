package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class ClientDevice(
    val id: Int = 0,
    val imei: String = "",
    val lastEnterDate: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val status: Boolean = false
)