package com.example.tezal.DBHelper

import android.media.MediaPlayer
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CartDao {
    @Query("Select * from Cart Where uid=:uid")
    fun getAllCart(uid:String): Flowable<List<CartItem>>

    @Query("Select COUNT(*) from Cart Where uid=:uid")
    fun countItemCart(uid:String): Single<Int>

    @Query("Select SUM((Price+ExtraPrice)*Quantity) from Cart Where uid=:uid")
    fun sumPrice(uid:String): Single<Double>

    @Query("Select * from Cart Where Id =:Id And uid=:uid ")
    fun getItemInCart(Id:String, uid:String):Single<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg cartItems:CartItem):Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCart(cart:CartItem):Single<Int>

    @Delete
    fun deleteCart(cart:CartItem):Single<Int>

    @Query("DELETE FROM Cart where uid = :uid")
    fun cleanCart(uid:String):Single<Int>
}