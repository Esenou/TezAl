package com.example.tezal.common

import com.example.tezal.model.AuthModel

object Common {
    fun formatPrice(price: Double): Any {
        return price
    }

    val USER_REFERENCE="Users"
    var currentUser: AuthModel? = null
}