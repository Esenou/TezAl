package com.example.tezal.ui.home.orgListCategory.rawMaterial

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.RawListItem

interface rawContract  {
    interface View:IProgressBar{
        fun onSuccssesGetRawList(list: List<RawListItem>)
        fun onFailure()
    }
    interface Presenter{
        fun getRawList(orgId:Int?)
    }
}