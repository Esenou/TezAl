package com.example.tezal.ui.share.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tezal.R
import com.example.tezal.model.RawHistory
import com.example.tezal.model.RawHistoryItem
import kotlinx.android.synthetic.main.item_activity_history.view.*
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryDetailAdapter(private var list: RawHistory): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity_history,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return   list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HistoryDetailAdapter.ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: RawHistoryItem){

            itemView.txtToProductNameHistoryDetails.text = position.rawMaterialName
            itemView.txtToProductPriceHistoryDetails.text = position.sum.toString()
            itemView.txtToProductCountHistoryDetails.text = position.count.toString()
            // Glide.with(itemView).load(position.image).into(itemView.imgCategory)


        }
    }


}