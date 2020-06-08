package com.example.tezal.ui.home.orgListCategory.rawMaterial.order

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tezal.DBHelper.CartDataSource
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.CartItem
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.common.Common
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartModel: ViewModel() {

    private val compositeDisposable: CompositeDisposable
    private var cardDataSource: CartDataSource? = null
    private var mutableLiveDataCartItem: MutableLiveData<List<CartItem>>?=null

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun initCartDataSource(context: Context) {
        cardDataSource = LocalCartDataSource(CartDatabase.getInstance(context = context).CartDao())
    }
    fun getMutableLiveDataCartItem(): MutableLiveData<List<CartItem>> {
        println("mutableLiveDataCartItem :"+mutableLiveDataCartItem)
        if (mutableLiveDataCartItem == null){
            mutableLiveDataCartItem = MutableLiveData()
        }
        getCartItems()
        return mutableLiveDataCartItem!!
    }

    fun onStop(){
        compositeDisposable.clear()
    }
    //private val cartItem:List<CartItem>?
    private fun getCartItems(){
        compositeDisposable.addAll(cardDataSource!!.getAllCart(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({cartItem ->
                mutableLiveDataCartItem!!.value = cartItem
            },{t: Throwable? -> mutableLiveDataCartItem!!.value = null }))
    }
}