package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class Client(
    val clientSex: String = "",
    val createdDate: String = "",
    val firstName: String = "",
    val id: Long = 0,
    val image: String = "",
    val lastName: String = "",
    val locale: String = "",
    val nationality: String = "",
    val patronymic: String = "",
    val personalCode: String = ""
)