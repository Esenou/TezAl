package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class OrgListCategoryItem(
    val categoryId: Int = 0,
    val categoryName: String = "",
    val description: String = "",
    val id: Int = 0,
    val image: String = "",
    val name: String = "",
    val status: Boolean = false
)