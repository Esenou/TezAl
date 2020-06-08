package com.example.tezal.ui.home.orgListCategory.rawMaterial

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tezal.DBHelper.CartDataSource
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.EventBus.CountCartEvent
import com.example.tezal.EventBus.HideFABCart
import com.example.tezal.MainActivity
import com.example.tezal.R
import com.example.tezal.common.Common
import com.example.tezal.model.OrderSql
import com.example.tezal.model.OrgListCategoryItem
import com.example.tezal.model.RawListItem
import com.example.tezal.ui.home.orgListCategory.badge.NotificationCountSetClass
import com.example.tezal.ui.home.orgListCategory.rawMaterial.order.OrderActivity
import com.google.gson.Gson
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_raw.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable


class rawActivity : AppCompatActivity(),rawContract.View,rawAdapter.Listener {


    lateinit var presenter: rawPresenter
    lateinit var adapter: rawAdapter

    var common: List<RawListItem> = mutableListOf<RawListItem>()


    var notificationCountCart = 0
    var oderList:MutableList<OrderSql> = mutableListOf<OrderSql>()



    var organizationId:Int?=null
    var rawMaterialName:String?=null

    private lateinit var cartDataSource:CartDataSource


    override fun onResume() {
        super.onResume()
        countCartItem()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_raw)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(this).CartDao())


        fab.setOnClickListener { view ->

            val intent = Intent(this,OrderActivity::class.java)
            startActivity(intent)

        }



        val bundle = intent.extras
        if (bundle != null) {
            val json = bundle.getString("key")
            val vh = Gson().fromJson<OrgListCategoryItem>(json, OrgListCategoryItem::class.java!!)

            println("----------------------------------------------")
            println("-->" + vh.id)
            println("-->" + vh.categoryId)
            println("----------------------------------------------")
            init(vh.id)

            Toast.makeText(this, "Item :" + vh.id, Toast.LENGTH_LONG).show()
        }


        countCartItem()

    }


    private fun init(orgId:Int?){
        presenter = rawPresenter(this)
        presenter.getRawList(orgId)
    }

    override fun onSuccssesGetRawList(list: List<RawListItem>) {
        common = list
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewRaw?.layoutManager = GridLayoutManager(this, 1)
        recyclerViewRaw?.layoutManager = layoutManager
        adapter = rawAdapter(list, this,this)
        recyclerViewRaw.adapter = adapter

    }





    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0);
    }

    override fun onFailure() {

    }



    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setOnItemClick(position: RawListItem) {
       // Toast.makeText(this, "item "+position.id, Toast.LENGTH_LONG).show()
        println("------------------------------------------")

        println("item "+position.organizationId)
        println("------------------------------------------")
    }



    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateItemInCart(even : HideFABCart) {

        if(even.isHide){
            fab.hide()
        }
        else{
            fab.show()
        }


    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateItemInCart(even : CountCartEvent) {
        if (even.isSuccess){
            countCartItem()

        }
    }
    override fun onStop() {
        super.onStop()

        if(adapter!=null){
            adapter!!.onStop()
        }
        EventBus.getDefault().unregister(this);
    }
    private fun countCartItem(){
        cartDataSource.countItemCart(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int>{
                override fun onSuccess(t: Int) {
                    println("FabCounter :"+t)
                    fab.count = t
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    if(!e.message!!.contains("Query returned empty"))
                        Toast.makeText(this@rawActivity,"[COUNT CART]"+e!!.message,Toast.LENGTH_SHORT).show()
                    else
                        fab.count = 0
                }

            })
    }
}







