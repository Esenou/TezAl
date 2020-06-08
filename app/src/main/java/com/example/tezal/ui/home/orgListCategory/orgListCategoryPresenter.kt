package com.example.tezal.ui.home.orgListCategory

import com.example.tezal.StartApplication
import com.example.tezal.model.OrgCategoryItem
import com.example.tezal.model.OrgListCategoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class orgListCategoryPresenter(val view: orgListCategoryContract.View):orgListCategoryContract.Presenter {

    override fun getOrgListCategory(id:Int?) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getOrgListCategory(id).enqueue(
                object : Callback<MutableList<OrgListCategoryItem>> {
                    override fun onFailure(
                        call: Call<MutableList<OrgListCategoryItem>>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<OrgListCategoryItem>>?,
                        response: Response<MutableList<OrgListCategoryItem>>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccssesGetOrgListCategory(response.body()!!)
                            } else
                                view.onFailure()
                            view.hideProgress()
                        }
                    }
                }
            )
        }
    }
    fun isViewAttached(): Boolean = view != null
}