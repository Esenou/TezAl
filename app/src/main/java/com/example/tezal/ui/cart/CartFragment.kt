package com.example.tezal.ui.cart


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
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
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.StringBuilder


class CartFragment : Fragment(),CartContract.View {

    var orderList:Order_first?=null

    lateinit var presenter: CartPresenter
    lateinit var sharedPreferences: SharedPreferences
    var listOrder:OrderList? = null
    var flag :Boolean = false
    var adapter:CartAdapter?=null
    private var cartDataSource:CartDataSource?=null
    private var compositeDisposable:CompositeDisposable = CompositeDisposable()
    private var recyclerViewState:Parcelable?= null
    private lateinit var cartViewModel:CartModel
    var txtEmptyCart:TextView? = null
    var txtTotalPrice:TextView? = null
    var groupPlaceHolder:CardView? = null
    var recyclerViewCart:RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().postSticky(HideFABCart(true))
        cartViewModel =  ViewModelProviders.of(this).get(CartModel::class.java)
        cartViewModel.initCartDataSource(context!!)
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        cartViewModel.getMutableLiveDataCartItem().observe(this, Observer {
            if (it == null || it.isEmpty()){
                recyclerViewCart!!.visibility = View.GONE
                groupPlaceHolder!!.visibility = View.GONE
                txtEmptyCart!!.visibility = View.VISIBLE

            } else {
                recyclerViewCart!!.visibility = View.VISIBLE
                groupPlaceHolder!!.visibility = View.VISIBLE
                txtEmptyCart!!.visibility = View.GONE
                adapter = CartAdapter(context!!,it)
                recyclerViewCart!!.adapter = adapter
            }
        })
        initViews(root)
        return root
    }

    private fun initViews(root:View) {
        setHasOptionsMenu(true)
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context!!).CartDao())

        recyclerViewCart = root.findViewById(R.id.recyclerViewCart) as RecyclerView
        recyclerViewCart!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerViewCart!!.layoutManager = layoutManager
        recyclerViewCart!!.addItemDecoration(DividerItemDecoration(context,layoutManager.orientation))

        val swipe = object : MySwipeHelper(context!!,recyclerViewCart!!,200)
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton(context!!,
                "Delete",
                30,
                0,
                Color.parseColor("#FF3c30"),
                object :IMyButtonCallback{
                    override fun onClick(pos: Int) {

                        val deleteItem = adapter!!.getItemAtPosition(pos)

                        cartDataSource!!.deleteCart(deleteItem)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object :SingleObserver<Int>{
                                override fun onSuccess(t: Int) {
                                    adapter!!.notifyItemRemoved(pos)
                                    sumCart()
                                    EventBus.getDefault().postSticky(CountCartEvent(true))
                                    Toast.makeText(context,"Delete item success",Toast.LENGTH_SHORT).show()
                                }

                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onError(e: Throwable) {
                                   Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                                }

                            })
                        println("Delete -------------------->")
                        Toast.makeText(context,"Delete Item",Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        }

        txtEmptyCart = root.findViewById(R.id.txtEmptyCart) as? TextView
        txtTotalPrice = root.findViewById(R.id.txtTotalPrice) as? TextView
        groupPlaceHolder = root.findViewById(R.id.group_place_holder) as CardView


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
                       Toast.makeText(context,""+e.message,Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context,"[UPDATE CART]"+e.message, Toast.LENGTH_SHORT).show()
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
                       Toast.makeText(context,"[SUM CART]"+e.message, Toast.LENGTH_SHORT).show()
               }
           })
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        btnPlaceOrder.setOnClickListener {
            Toast.makeText(context,"Ok",Toast.LENGTH_SHORT).show()
            payment()
        }

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
                            Toast.makeText(context!!,""+e.message,Toast.LENGTH_SHORT).show()
                        }
                    })
            },{throwable -> Toast.makeText(context!!,""+throwable.message,Toast.LENGTH_SHORT).show()})
        )
    }

    private fun writeOrderToApi(orde: Order_first) {
        sharedPreferences = getActivity()?.getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)!!;
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
        presenter = CartPresenter(this)
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
            presenter = CartPresenter(this)
            presenter.getOrderMaterial(orde!!)
        }

        cartDataSource!!.cleanCart(Common.USER_REFERENCE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :SingleObserver<Int>{
                override fun onSuccess(t: Int) {
                    Toast.makeText(context!!,"Order placed successfuly",Toast.LENGTH_SHORT).show()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context!!,""+e.message,Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
          //  menu!!.findItem(R.id.action_settings).setVisible(false)
        super.onPrepareOptionsMenu(menu)
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.cart_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.actionClearCart){
            cartDataSource!!.cleanCart(Common.USER_REFERENCE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSuccess(price: Int) {
                        Toast.makeText(context,"Clear Cart Success ", Toast.LENGTH_SHORT).show()
                        EventBus.getDefault().postSticky(CountCartEvent(true))
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {

                            Toast.makeText(context,""+e.message, Toast.LENGTH_SHORT).show()
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