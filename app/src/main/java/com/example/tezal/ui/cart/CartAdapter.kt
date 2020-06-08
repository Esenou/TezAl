package com.example.tezal.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.tezal.DBHelper.CartDataSource
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.CartItem
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_raw.view.*
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

class CartAdapter(internal var context: Context,internal var cartItems: List<CartItem>):RecyclerView.Adapter<CartAdapter.MyViewHolder>() {


    lateinit var compositeDisposable: CompositeDisposable
    lateinit var cardDataSource: CartDataSource

    init {
        compositeDisposable = CompositeDisposable()
        cardDataSource = LocalCartDataSource(CartDatabase.getInstance(context = context).CartDao())
    }
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var imgCart:ImageView
        lateinit var txtName:TextView
        lateinit var txtPrice:TextView
        lateinit var numberButton: ElegantNumberButton
        init {
            imgCart = itemView.findViewById(R.id.imgCart) as ImageView
            txtName = itemView.findViewById(R.id.txtToProductName) as TextView
            txtPrice = itemView.findViewById(R.id.txtToProductPrice) as TextView
            numberButton = itemView.findViewById(R.id.btnProductCount) as ElegantNumberButton

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_cart_item, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return cartItems.size
    }

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        holder.txtName.text = StringBuilder(cartItems[position].Name!!)
        holder.txtPrice.text = StringBuilder("").append(cartItems[position].Price + cartItems[position].ExtraPrice)
        holder.numberButton.number = cartItems[position].Quantity.toString()
        Glide.with(holder.itemView).load(cartItems[position].Image).into(holder.imgCart)
        holder.numberButton.setOnValueChangeListener { view, oldValue, newValue ->
            cartItems[position].Quantity = newValue
            EventBus.getDefault().postSticky(
                UpdateItemCart(
                    cartItems[position]
                )
            )
        }
    }

    fun getItemAtPosition(pos: Int): CartItem {
        return cartItems[pos]
    }
}