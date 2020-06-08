package com.example.tezal.ui.home.orgListCategory.rawMaterial

import com.example.tezal.StartApplication
import com.example.tezal.model.RawListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class rawPresenter(val view:rawContract.View):rawContract.Presenter {
    override fun getRawList(orgId: Int?) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getRawList(orgId).enqueue(
                object : Callback<MutableList<RawListItem>> {
                    override fun onFailure(
                        call: Call<MutableList<RawListItem>>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<RawListItem>>?,
                        response: Response<MutableList<RawListItem>>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccssesGetRawList(response.body()!!)
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