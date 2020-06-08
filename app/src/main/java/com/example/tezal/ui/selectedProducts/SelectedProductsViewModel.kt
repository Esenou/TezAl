package com.example.tezal.ui.selectedProducts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.org.LocalOrgDataSource
import com.example.tezal.DBHelper.org.OrgDataSource
import com.example.tezal.DBHelper.org.OrgItem
import com.example.tezal.common.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectedProductsViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable
    private var cardDataSource: OrgDataSource? = null
    private var mutableLiveDataCartItem: MutableLiveData<List<OrgItem>>?=null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun initCartDataSource(context: Context) {
        cardDataSource = LocalOrgDataSource(CartDatabase.getInstance(context = context).OrgDao())
    }
    fun getMutableLiveDataCartItem(): MutableLiveData<List<OrgItem>> {
        println("mutableLiveDataCartItem :"+mutableLiveDataCartItem)
        if (mutableLiveDataCartItem == null){
            mutableLiveDataCartItem = MutableLiveData()
        }
        getOrgItems()
        return mutableLiveDataCartItem!!
    }

    fun onStop(){
        compositeDisposable.clear()
    }
    //private val cartItem:List<CartItem>?
    private fun getOrgItems(){
        compositeDisposable.addAll(cardDataSource!!.getAllOrg(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({cartItem ->
                mutableLiveDataCartItem!!.value = cartItem
            },{t: Throwable? -> mutableLiveDataCartItem!!.value = null }))
    }
}