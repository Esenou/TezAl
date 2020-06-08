package com.example.tezal.ui.home.orgListCategory.rawMaterial.order


import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.Order
import com.example.tezal.model.OrderList
import com.example.tezal.model.Order_material
import com.example.tezal.model.ResponseOrder_material

interface OrderContract {
    interface View: IProgressBar {
        fun onSuccssesGetOrder(list: OrderList)
        fun onSuccssesGetOrderMaterial(list: ResponseOrder_material)
        fun onFailure()
    }
    interface Presenter{
        fun getOrderList(order: Order)
        fun getOrderMaterial(order: Order_material)
    }
}