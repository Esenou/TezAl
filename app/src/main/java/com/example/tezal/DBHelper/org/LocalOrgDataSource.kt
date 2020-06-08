package com.example.tezal.DBHelper.org

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalOrgDataSource(private val orgDAO:OrgDao):OrgDataSource  {

    override fun getAllOrg(uid: String): Flowable<List<OrgItem>> {
        return orgDAO.getAllOrg(uid)
    }

    override fun getItemInOrg(Id: String, uid: String): Single<OrgItem> {
        return orgDAO.getItemInOrg(Id,uid)
    }

    override fun insertOrReplaceAll(vararg orgItems: OrgItem): Completable {
        return orgDAO.insertOrReplaceAll(*orgItems)
    }

    override fun updateOrg(org: OrgItem): Single<Int> {
        return orgDAO.updateOrg(org)
    }

    override fun deleteOrg(org: OrgItem): Single<Int> {
        return orgDAO.deleteOrg(org)
    }


    override fun cleanOrg(uid: String): Single<Int> {
        return orgDAO.cleanOrg(uid)
    }
}