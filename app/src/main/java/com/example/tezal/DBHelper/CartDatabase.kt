package com.example.tezal.DBHelper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tezal.DBHelper.org.OrgDao
import com.example.tezal.DBHelper.org.OrgItem

@Database(version = 1, entities = [CartItem::class,OrgItem::class] ,exportSchema = false)
abstract class CartDatabase: RoomDatabase() {
    abstract fun CartDao():CartDao
    abstract fun OrgDao():OrgDao
    companion object{
        private var instance:CartDatabase? = null

        fun getInstance(context: Context):CartDatabase{
            if(instance == null)
                instance = Room.databaseBuilder<CartDatabase>(context,CartDatabase::class.java,"EatItV2001").build()
            return instance!!
        }
    }
}