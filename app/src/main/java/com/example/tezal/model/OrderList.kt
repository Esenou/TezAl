package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class OrderList(
    val client: Client = Client(),
    val createDate: String = "",
    val deadlineDate: Any = Any(),
    val id: Int = 0,
    val ordersStatus: String = "",
    val updateDate: String = ""
)