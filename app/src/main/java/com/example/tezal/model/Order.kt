package com.example.tezal.model

import com.example.tezal.DBHelper.CartItem


data class Order(
    val clientId: Long = 0,
    val ordersStatus: String = "",
    val organizationId: Int = 0
)