package com.example.tezal.model


data class Order_material(
    val count: Int = 0,
    val orderId: Int = 0,
    val rawMaterialId: Int = 0,
    var sum: Double? = 0.0
)