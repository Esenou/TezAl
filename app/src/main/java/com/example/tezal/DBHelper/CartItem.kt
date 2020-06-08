package com.example.tezal.DBHelper

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Cart")
class CartItem {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Id")
    var Id:String = ""
    @ColumnInfo(name = "Name")
    var Name:String? = null

    @ColumnInfo(name = "Image")
    var Image:String? = null

    @ColumnInfo(name = "rawMaterialId")
    var rawMaterialId: Int = 0

    @ColumnInfo(name = "Price")
    var Price: Double = 0.0
    @ColumnInfo(name = "Quantity")
    var Quantity:Int = 0
    @ColumnInfo(name = "Addon")
    var Addon:String? = ""
    @ColumnInfo(name = "Size")
    var Size :String? = ""
    @ColumnInfo(name = "UserPhone")
    var UserPhone :String? = ""
    @ColumnInfo(name = "ExtraPrice")
    var ExtraPrice :Double = 0.0
    @ColumnInfo(name = "Uid")
    var Uid :String? = ""

}