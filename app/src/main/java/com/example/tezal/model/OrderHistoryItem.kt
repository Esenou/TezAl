package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class OrderHistoryItem(
    val clientFirstName: String = "",
    val clientId: Int = 0,
    val clientLastName: String = "",
    val id: Long = 0,
    val ordersStatus: String = "",
    val organizationId: Int = 0,
    val organizationName: String = "",
    val updatedDate: String = "",
    val userId: Long = 0
)