package com.example.tezal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tezal.R
import com.example.tezal.model.OrgCategoryItem

import kotlinx.android.synthetic.main.item_category.view.*

class HomeAdapter (private var list: List<OrgCategoryItem>, val listener:Listener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return   list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HomeAdapter.ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: OrgCategoryItem){

            itemView.nameCategory.text=position.name
            Glide.with(itemView).load(position.image).into(itemView.imgCategory)
            itemView.tag = position
            itemView.setOnClickListener { v ->
                val position = v.tag as OrgCategoryItem
                listener.setOnItemClick(position)

            }

        }
    }

    interface Listener{
        fun setOnItemClick(position: OrgCategoryItem)

    }
}