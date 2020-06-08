package com.example.tezal.ui.home.orgListCategory.rawMaterial.order


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tezal.Callback.IMyButtonCallback
import com.example.tezal.DBHelper.CartDataSource
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.EventBus.CountCartEvent
import com.example.tezal.EventBus.HideFABCart
import com.example.tezal.R
import com.example.tezal.common.Common
import com.example.tezal.common.MySwipeHelper
import com.example.tezal.model.*
import com.example.tezal.ui.cart.CartAdapter
import com.example.tezal.ui.cart.CartModel
import com.example.tezal.ui.cart.CartPresenter
import com.example.tezal.ui.cart.UpdateItemCart
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_oreder.*
import kotlinx.android.synthetic.main.activity_oreder.btnPlaceOrder
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder


class OrderActivity : AppCompatActivity(),OrderContract.View {


    var orderList:Order_first?=null

    lateinit var presenter: OrderPresenter
    lateinit var sharedPreferences: SharedPreferences
    var listOrder:OrderList? = null
    var flag :Boolean = false
    var adapter: CartAdapter?=null
    private var cartDataSource: CartDataSource?=null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var recyclerViewState: Parcelable?= null
    private lateinit var cartViewModel: CartModel
    var txtEmptyCart: TextView? = null
    var txtTotalPrice: TextView? = null
    var groupPlaceHolder: CardView? = null
    var recyclerViewCart: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oreder)

        EventBus.getDefault().postSticky(HideFABCart(true))
        cartViewModel =  ViewModelProviders.of(this).get(CartModel::class.java)
        cartViewModel.initCartDataSource(this)

        cartViewModel.getMutableLiveDataCartItem().observe(this, Observer {
            if (it == null || it.isEmpty()){
                recyclerViewCart!!.visibility = View.GONE
                groupPlaceHolder!!.visibility = View.GONE
                txtEmptyCart!!.visibility = View.VISIBLE

            } else {
                recyclerViewCart!!.visibility = View.VISIBLE
                groupPlaceHolder!!.visibility = View.VISIBLE
                txtEmptyCart!!.visibility = View.GONE
                adapter = CartAdapter(this,it)
                recyclerViewCart!!.adapter = adapter
            }
        })
        initViews()
        btnPlaceOrder.setOnClickListener {
            Toast.makeText(this,"Ok",Toast.LENGTH_SHORT).show()
            payment()
        }

    }
    private fun initViews() {

        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(this).CartDao())

        recyclerViewCart = findViewById(R.id.recyclerViewCart) as RecyclerView
        recyclerViewCart!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerViewCart!!.layoutManager = layoutManager
        recyclerViewCart!!.addItemDecoration(DividerItemDecoration(this,layoutManager.orientation))

        val swipe = object : MySwipeHelper(this,recyclerViewCart!!,200)
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton( this@OrderActivity,
                    "Delete",
                    30,
                    0,
                    Color.parseColor("#FF3c30"),
                    object : IMyButtonCallback {
                        override fun onClick(pos: Int) {

                            val deleteItem = adapter!!.getItemAtPosition(pos)

                            cartDataSource!!.deleteCart(deleteItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<Int> {
                                    override fun onSuccess(t: Int) {
                                        adapter!!.notifyItemRemoved(pos)
                                        sumCart()
                                        EventBus.getDefault().postSticky(CountCartEvent(true))
                                        Toast.makeText(this@OrderActivity,"Delete item success",Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onSubscribe(d: Disposable) {

                                    }

                                    override fun onError(e: Throwable) {
                                        Toast.makeText(this@OrderActivity, e.message, Toast.LENGTH_SHORT).show()
                                    }

                                })
                            println("Delete -------------------->")
                            Toast.makeText(this@OrderActivity,"Delete Item",Toast.LENGTH_SHORT).show()
                        }
                    }))
            }
        }

        txtEmptyCart = findViewById(R.id.txtEmptyCart) as? TextView
        txtTotalPrice = findViewById(R.id.txtTotalPrice) as? TextView
        groupPlaceHolder = findViewById(R.id.group_place_holder) as CardView


    }
    private fun sumCart() {
        cartDataSource!!.sumPrice(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:SingleObserver<Double>{
                override fun onSuccess(t: Double) {
                    txtTotalPrice!!.text = StringBuilder("Total: ")
                        .append(t)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    if (!e.message!!.contains("Query returned empty"))
                        Toast.makeText(this@OrderActivity,""+e.message,Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onStart() {
        super.onStart()
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        cartViewModel!!.onStop()
        compositeDisposable!!.clear()
        EventBus.getDefault().postSticky(HideFABCart(false))
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)

    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onUpdateItemInCart(even : UpdateItemCart) {
        if(even.cartItem != null) {
            recyclerViewState = recyclerViewCart!!.layoutManager!!.onSaveInstanceState()
            cartDataSource!!.updateCart(even.cartItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSuccess(t: Int) {
                        calculateTotalPrice();
                        recyclerViewCart!!.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(this@OrderActivity,"[UPDATE CART]"+e.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    fun calculateTotalPrice(){
        cartDataSource!!.sumPrice(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Double> {
                override fun onSuccess(price: Double) {
                    txtTotalPrice!!.text = StringBuilder("Total: ")
                        .append(Common.formatPrice(price))
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    if(!e.message!!.contains("Query returned empty"))
                        Toast.makeText(this@OrderActivity,"[SUM CART]"+e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }





    private fun payment(){
        compositeDisposable.add(cartDataSource!!.getAllCart(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ cartItemList ->

                cartDataSource!!.sumPrice(Common.USER_REFERENCE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :SingleObserver<Double>{
                        override fun onSuccess(totalPrice: Double) {
                            val finalPrice = totalPrice
                            val order = Order_first()
                            order.cartItemList = cartItemList
                            order.sum = totalPrice
                            orderList = Order_first()
                            orderList?.cartItemList = cartItemList
                            writeOrderToApi(order)
                        }

                        override fun onSubscribe(d: Disposable) {

                        }
                        override fun onError(e: Throwable) {
                            Toast.makeText(this@OrderActivity,""+e.message,Toast.LENGTH_SHORT).show()
                        }
                    })
            },{throwable -> Toast.makeText(this@OrderActivity,""+throwable.message,Toast.LENGTH_SHORT).show()})
        )
    }

    private fun writeOrderToApi(orde: Order_first) {
        sharedPreferences = getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)!!;
        val getId = sharedPreferences?.getLong("clientId",0)
        val organizationId = sharedPreferences?.getInt("organizationId",0)
        val order = Order(
            getId,
            "AWAITING",
            organizationId
        )
        init(order)



    }
    private fun init(order:Order){
        println("333")
        presenter = OrderPresenter(this)
        presenter.getOrderList(order)

    }

    private fun initTwo(order: OrderList){
        var orde:Order_material?=null
        println("--------------forEach-------------->")

        orderList?.cartItemList?.forEach {
            println(it)
            orde = Order_material(
                it.Quantity,
                listOrder!!.id,
                it.rawMaterialId,
                it.Price*it.Quantity)
            presenter = OrderPresenter(this)
            presenter.getOrderMaterial(orde!!)
        }

        cartDataSource!!.cleanCart(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :SingleObserver<Int>{
                override fun onSuccess(t: Int) {
                    Toast.makeText(this@OrderActivity,"Order placed successfuly",Toast.LENGTH_SHORT).show()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@OrderActivity,""+e.message,Toast.LENGTH_SHORT).show()
                }

            })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater:MenuInflater
        inflater = menuInflater
        inflater.inflate(R.menu.cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.actionClearCart){
            cartDataSource!!.cleanCart(Common.USER_REFERENCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSuccess(price: Int) {
                        Toast.makeText(this@OrderActivity,"Clear Cart Success ", Toast.LENGTH_SHORT).show()
                        EventBus.getDefault().postSticky(CountCartEvent(true))
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {

                        Toast.makeText(this@OrderActivity,""+e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSuccssesGetOrder(list: OrderList) {
        flag = true
        listOrder = list
        initTwo(list)
        println("-------------------------------------->")
        println(listOrder!!.id)
        println("-------------------------------------->")
    }

    override fun onSuccssesGetOrderMaterial(list: ResponseOrder_material) {

    }

    override fun onFailure() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}
