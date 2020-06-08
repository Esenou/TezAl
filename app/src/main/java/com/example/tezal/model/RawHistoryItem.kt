package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class RawHistoryItem(
    val count: Int = 0,
    val id: Int = 0,
    val orderId: Int = 0,
    val rawMaterialId: Int = 0,
    val rawMaterialName: String = "",
    val rawMaterialVolume: Int = 0,
    val sum: Int = 0
)