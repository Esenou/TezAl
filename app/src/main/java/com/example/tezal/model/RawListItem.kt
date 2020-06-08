package com.example.tezal.model


import com.google.gson.annotations.SerializedName

data class RawListItem(
    val id: Int = 0,
    val organizationId: Int = 0,
    val organizationName: String = "",
    val quantityInStock: Double = 0.0,
    val rawMaterialCategory: String = "",
    val rawMaterialId: Int = 0,
    val rawMaterialName: String = "",
    val retailPrice: Double = 0.0,
    val wholesalePrice: Double = 0.0,
    val rawMaterialImage:String = ""
)