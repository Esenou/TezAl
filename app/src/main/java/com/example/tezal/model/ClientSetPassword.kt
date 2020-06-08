package com.example.tezal.model


data class ClientSetPassword(
    val client: ClientId? = ClientId(),
    val imei: String? = "",
    val lastEnterDate: String? = "",
    val password: String? = "",
    val phoneNumber: String? = "",
    val status: Boolean?
)