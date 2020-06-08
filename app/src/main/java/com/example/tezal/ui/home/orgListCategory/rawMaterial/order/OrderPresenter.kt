package com.example.tezal.ui.home.orgListCategory.rawMaterial.order


import com.example.tezal.StartApplication
import com.example.tezal.model.Order
import com.example.tezal.model.OrderList
import com.example.tezal.model.Order_material
import com.example.tezal.model.ResponseOrder_material
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderPresenter(val view:OrderContract.View):OrderContract.Presenter {
    override fun getOrderList(order: Order) {

        if (isViewAttached()) {
        view.showProgress()
        StartApplication.service.setDate(order).enqueue(
            object : Callback<OrderList> {
                override fun onFailure(
                    call: Call<OrderList>?,
                    t: Throwable?
                ) {
                    if (isViewAttached()) {
                        view.hideProgress()
                    }
                    t?.printStackTrace()
                }

                override fun onResponse(
                    call: Call<OrderList>?,
                    response: Response<OrderList>?
                ) {
                    if (isViewAttached()) {
                        if (response!!.isSuccessful && response != null) {
                            view.onSuccssesGetOrder(response.body()!!)
                        } else
                            view.onFailure()
                        view.hideProgress()
                    }
                }
            }
        )
    }
}



    override fun getOrderMaterial(order: Order_material) {
        if (isViewAttached()) {
            view.showProgress()
            StartApplication.service.setDate(order).enqueue(
                object : Callback<ResponseOrder_material> {
                    override fun onFailure(
                        call: Call<ResponseOrder_material>?,
                        t: Throwable?
                    ) {
                        if (isViewAttached()) {
                            view.hideProgress()
                        }
                        t?.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ResponseOrder_material>?,
                        response: Response<ResponseOrder_material>?
                    ) {
                        if (isViewAttached()) {
                            if (response!!.isSuccessful && response != null) {
                                view.onSuccssesGetOrderMaterial(response.body()!!)
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