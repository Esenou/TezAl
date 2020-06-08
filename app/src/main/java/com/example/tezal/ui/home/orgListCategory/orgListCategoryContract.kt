package com.example.tezal.ui.home.orgListCategory

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.OrgListCategoryItem

interface orgListCategoryContract {

    interface View:IProgressBar{
        fun onSuccssesGetOrgListCategory(list: List<OrgListCategoryItem>)
        fun onFailure()
    }
    interface Presenter{
        fun getOrgListCategory(id:Int?)
    }

}