package com.example.tezal.ui.home.orgListCategory.rawMaterial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tezal.DBHelper.CartDataSource
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.CartItem
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.EventBus.CountCartEvent
import com.example.tezal.R
import com.example.tezal.common.Common
import com.example.tezal.model.OrderSql
import com.example.tezal.model.RawListItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_raw.view.*
import org.greenrobot.eventbus.EventBus

class rawAdapter (private var list: List<RawListItem>, internal  val context: Context, val listener:Listener): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private val compositeDisposable: CompositeDisposable
    private val cardDataSource:CartDataSource

    init {
        compositeDisposable = CompositeDisposable()
        cardDataSource = LocalCartDataSource(CartDatabase.getInstance(context = context).CartDao())
    }
    var oderList:MutableList<OrderSql> = mutableListOf<OrderSql>()
    //private  var listItem:List<CardItem>? = null
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_raw, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as rawAdapter.ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: RawListItem) {

            itemView.nameRaw.text = position.rawMaterialName
            Glide.with(itemView).load(position.rawMaterialImage).into(itemView.imgRaw)
            itemView.tag = position
            var state:Boolean = false;
            val orderSql = OrderSql(
                position.id,
                position.organizationId,
                position.rawMaterialId,
                position.rawMaterialName,
                position.retailPrice,
                position.wholesalePrice
            )

            itemView.btnAddToCard.setOnClickListener {
                val cardItem = CartItem()
                cardItem.Uid = Common.USER_REFERENCE
                cardItem.Id = position.id.toString()
                cardItem.Name = position.rawMaterialName
                cardItem.rawMaterialId = position.rawMaterialId
                cardItem.Image = position.rawMaterialImage
                cardItem.Price = position.wholesalePrice!!.toDouble()
                cardItem.Quantity = 1
                cardItem.ExtraPrice = 0.0
                cardItem.Size = "Default"
                cardItem.Addon = "Default"

                compositeDisposable.add(cardDataSource.insertOrReplaceAll(cardItem)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(context,"Add to card success",Toast.LENGTH_SHORT).show()
                        // Here we will send  a notify to rawActivity to update Counter
                        EventBus.getDefault().postSticky(CountCartEvent(true))
                    },{
                        t:Throwable?->  Toast.makeText(context,"[INSERT CART]"+t!!.message,Toast.LENGTH_SHORT).show()
                    }))


            }
            }

        }

       fun onStop(){
           if(compositeDisposable != null){
               compositeDisposable.clear()
           }
       }
    interface Listener {
        fun setOnItemClick(position: RawListItem)

    }
}
