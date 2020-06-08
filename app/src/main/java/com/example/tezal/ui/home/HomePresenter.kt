package com.example.tezal.ui.home

import com.example.tezal.StartApplication
import com.example.tezal.model.OrgCategoryItem
import com.example.tezal.model.ProdCatergoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(val view: HomeContract.View):HomeContract.Presenter {
    override fun getCategory() {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getCategory().enqueue(
                object : Callback<MutableList<OrgCategoryItem>> {
                    override fun onFailure(
                        call: Call<MutableList<OrgCategoryItem>>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<OrgCategoryItem>>?,
                        response: Response<MutableList<OrgCategoryItem>>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccssesGetCategory(response.body()!!)
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