package com.example.tezal.ui.share.details

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.RawHistory

interface HistoryDetailContract {
    interface View: IProgressBar {
        fun onSuccssesGetHistory(list: RawHistory)
        fun onFailure()
    }
    interface Presenter{
        fun getRaw(id:Long)
    }
}