package com.example.tezal.ui.home.orgListCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.LocalCartDataSource
import com.example.tezal.DBHelper.org.LocalOrgDataSource
import com.example.tezal.DBHelper.org.OrgDataSource
import com.example.tezal.DBHelper.org.OrgItem
import com.example.tezal.EventBus.CountCartEvent
import com.example.tezal.R
import com.example.tezal.common.Common

import com.example.tezal.model.OrgListCategoryItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_org_list_category.view.*
import org.greenrobot.eventbus.EventBus

class orgListCategoryAdapter  (private var list: List<OrgListCategoryItem>,val context: Context, val listener:Listener): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private val compositeDisposable: CompositeDisposable
    private val orgDataSource: OrgDataSource

    init {
        compositeDisposable = CompositeDisposable()
        orgDataSource = LocalOrgDataSource(CartDatabase.getInstance(context = context).OrgDao())
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_org_list_category,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return   list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as orgListCategoryAdapter.ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: OrgListCategoryItem){

            itemView.nameOrgListCategory.text=position.name
            Glide.with(itemView).load(position.image).into(itemView.imgOrgListCategory)
            itemView.tag = position
            itemView.setOnClickListener { v ->
                val position = v.tag as OrgListCategoryItem
                listener.setOnItemClick(position)

            }
            itemView.ratingBar_bookmarks.setOnRatingBarChangeListener { ratingBar, fl, b ->
                val orgItem = OrgItem()
                orgItem.Uid = Common.USER_REFERENCE
                orgItem.categoryId = position.categoryId
                orgItem.categoryName = position.categoryName
                orgItem.image  = position.image
                orgItem.id = position.id.toString()
                orgItem.name = position.name

                compositeDisposable.add(orgDataSource.insertOrReplaceAll(orgItem)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(context,"Add to card success",Toast.LENGTH_SHORT).show()
                        // Here we will send  a notify to rawActivity to update Counter
                        //  EventBus.getDefault().postSticky(CountCartEvent(true))
                    },{
                            t:Throwable?->  Toast.makeText(context,"[INSERT CART]"+t!!.message,Toast.LENGTH_SHORT).show()
                    }))
            }

        }
    }

    interface Listener{
        fun setOnItemClick(position: OrgListCategoryItem)

    }

    override fun getFilter(): Filter {
        return filter
    }

}