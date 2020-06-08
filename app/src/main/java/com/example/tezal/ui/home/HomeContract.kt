package com.example.tezal.ui.home

import app.superesenou.ru.example_neobis_translate.main.utils.IProgressBar
import com.example.tezal.model.OrgCategoryItem


interface HomeContract {
    interface View: IProgressBar {

        fun onSuccssesGetCategory(list: List<OrgCategoryItem>)
        fun onFailure()
    }
    interface Presenter{
        fun getCategory()
    }
}
