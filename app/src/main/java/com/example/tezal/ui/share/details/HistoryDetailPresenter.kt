package com.example.tezal.ui.share.details

import com.example.tezal.StartApplication
import com.example.tezal.model.RawHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetailPresenter(val view:HistoryDetailContract.View):HistoryDetailContract.Presenter{
    override fun getRaw(id: Long) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getHistoryDetails(id).enqueue(
                object : Callback<RawHistory> {
                    override fun onFailure(
                        call: Call<RawHistory>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<RawHistory>?,
                        response: Response<RawHistory>?
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