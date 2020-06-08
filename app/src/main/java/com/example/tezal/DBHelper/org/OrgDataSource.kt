package com.example.tezal.DBHelper.org

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface OrgDataSource {

    fun getAllOrg(uid:String): Flowable<List<OrgItem>>

    fun getItemInOrg(Id:String, uid:String): Single<OrgItem>


    fun insertOrReplaceAll(vararg orgItems: OrgItem): Completable


    fun updateOrg(org: OrgItem): Single<Int>


    fun deleteOrg(org: OrgItem): Single<Int>


    fun cleanOrg(uid:String): Single<Int>
}