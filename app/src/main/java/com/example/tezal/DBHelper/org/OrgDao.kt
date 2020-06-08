package com.example.tezal.DBHelper.org

import androidx.room.*
import com.example.tezal.DBHelper.CartItem
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface OrgDao {
    @Query("Select * from Org Where uid=:uid")
    fun getAllOrg(uid:String): Flowable<List<OrgItem>>


    @Query("Select * from Org Where Id =:Id And uid=:uid ")
    fun getItemInOrg(Id:String, uid:String): Single<OrgItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg orgItems: OrgItem): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrg(org: OrgItem): Single<Int>

    @Delete
    fun deleteOrg(org: OrgItem): Single<Int>

    @Query("DELETE FROM Org where uid = :uid")
    fun cleanOrg(uid:String): Single<Int>
}