package com.example.tezal.model

import com.example.tezal.DBHelper.CartItem

class Order_first (
    val count: Int = 0,
    val orderId: Int = 0,
    val rawMaterialId: Int = 0,
    var sum: Double? = 0.0,
    var cartItemList: List<CartItem>? = null
)
