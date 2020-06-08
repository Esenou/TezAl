package com.example.tezal.ui.share

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.OrderHistoryItem

interface HistoryContract {
    interface View:IProgressBar{
        fun onSuccssesGetHistory(list: List<OrderHistoryItem>)
        fun onFailure()
    }
    interface Presenter{
        fun getOrganization(id:Long)
    }
}