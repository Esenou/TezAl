package com.example.tezal.DBHelper.org

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Org")
class OrgItem {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id:String = ""

    @ColumnInfo(name = "name")
    var name:String? = null

    @ColumnInfo(name = "categoryName")
    var categoryName:String? = null

    @ColumnInfo(name = "categoryId")
    var categoryId:Int = 0

    @ColumnInfo(name = "image")
    var image:String? = null

    @ColumnInfo(name = "Uid")
    var Uid :String? = ""

}