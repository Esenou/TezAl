package com.example.tezal.ui.share

import com.example.tezal.StartApplication
import com.example.tezal.model.OrderHistoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryPresenter(val view: HistoryContract.View):HistoryContract.Presenter  {
    override fun getOrganization(id:Long) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getHistory(id).enqueue(
                object : Callback<List<OrderHistoryItem>> {
                    override fun onFailure(
                        call: Call<List<OrderHistoryItem>>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<List<OrderHistoryItem>>?,
                        response: Response<List<OrderHistoryItem>>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccssesGetHistory(response.body()!!)
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