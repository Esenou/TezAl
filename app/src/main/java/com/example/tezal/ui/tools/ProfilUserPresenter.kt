package com.example.tezal.ui.tools

import com.example.tezal.StartApplication
import com.example.tezal.model.Client
import com.example.tezal.model.ClientDevice
import com.example.tezal.model.ClientInfo
import com.example.tezal.model.ClientResponseAfterSetPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ProfilUserPresenter(val view: ProfilUserContract.View):ProfilUserContract.Presenter {

    override fun setDateUserId(id: Long) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.getUser(id).enqueue(
                object : Callback<ClientResponseAfterSetPassword> {
                    override fun onFailure(
                        call: Call<ClientResponseAfterSetPassword>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ClientResponseAfterSetPassword>?,
                        response: Response<ClientResponseAfterSetPassword>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccessesGet(response.body()!!)
                            } else
                                view.onFailure()
                            view.hideProgress()
                        }
                    }
                }
            )
        }
    }

    override fun setDateUserIdForClient(id: Long, obj: ClientInfo) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.setDate(id,obj).enqueue(
                object : Callback<ClientResponseAfterSetPassword> {
                    override fun onFailure(
                        call: Call<ClientResponseAfterSetPassword>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ClientResponseAfterSetPassword>?,
                        response: Response<ClientResponseAfterSetPassword>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccessesGetClientInfo(response.body()!!)
                            } else
                                view.onFailure()
                            view.hideProgress()
                        }
                    }
                }
            )
        }
    }

    override fun setDateUserPassword(id: Long, obj: ClientDevice) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.setDate(id,obj).enqueue(
                object : Callback<ClientResponseAfterSetPassword> {
                    override fun onFailure(
                        call: Call<ClientResponseAfterSetPassword>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ClientResponseAfterSetPassword>?,
                        response: Response<ClientResponseAfterSetPassword>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccessesGetClientChanePassword(response.body()!!)
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