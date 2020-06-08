package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class ClientX(
    val clientSex: String = "",
    val createdDate: String = "",
    val firstName: String = "",
    val id: Int = 0,
    val image: Any = Any(),
    val lastName: String = "",
    val locale: String = "",
    val nationality: String = "",
    val patronymic: String = "",
    val personalCode: String = ""
)