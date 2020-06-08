package com.example.tezal.model

import java.io.Serializable

class OrderSql:Serializable {
    var id: Int = 0
    var organizationId: Int = 0
    var rawMaterialId: Int = 0
    var rawMaterialName: String? = null
    var retailPrice: Double = 0.0
    var wholesalePrice: Double = 0.0

    constructor(
        id: Int,
        organizationId: Int,
        rawMaterialId: Int,
        rawMaterialName: String?,
        retailPrice: Double,
        wholesalePrice: Double
    ) {
        this.id = id
        this.organizationId = organizationId
        this.rawMaterialId = rawMaterialId
        this.rawMaterialName = rawMaterialName
        this.retailPrice = retailPrice
        this.wholesalePrice = wholesalePrice
    }

    constructor()


}