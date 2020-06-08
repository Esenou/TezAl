package com.example.tezal.ui.share

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tezal.R
import com.example.tezal.model.OrderHistoryItem
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(private var list: List<OrderHistoryItem>, val listener:Listener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return   list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HistoryAdapter.ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(position: OrderHistoryItem){

            itemView.txtOrgNameHistory.text=position.organizationName
            if("AWAITING" == position.ordersStatus){
                itemView.txtOrgStatusHistory.text = "ЗАКАЗ В ОЖИДАНИИ..."
                itemView.txtOrgStatusHistory.setTextColor(Color.parseColor("#2EFB27"))
            } else if("ACCEPT" == position.ordersStatus){
                itemView.txtOrgStatusHistory.text = "ЗАКАЗ ПРИНЯТЬ..."
                itemView.txtOrgStatusHistory.setTextColor(Color.parseColor("#3393FF"))
            } else if("READY" == position.ordersStatus ){
                itemView.txtOrgStatusHistory.text = "ЗАКАЗ ГОТОВ..."
                itemView.txtOrgStatusHistory.setTextColor(Color.parseColor("#FFC300"))
            } else  if("DECLINED" == position.ordersStatus){
                itemView.txtOrgStatusHistory.text = "ЗАКАЗ ОТКЛОНЕН..."
                itemView.txtOrgStatusHistory.setTextColor(Color.parseColor("#FF3633"))
            } else {
                itemView.txtOrgStatusHistory.text = "ЗАКАЗ ВЫПОЛНЕНО..."
                itemView.txtOrgStatusHistory.setTextColor(Color.parseColor("#D9D4D4"))
            }
            itemView.txtOrgDataHistory.text = position.updatedDate.toString()
           // Glide.with(itemView).load(position.image).into(itemView.imgCategory)
            itemView.tag = position
            itemView.setOnClickListener { v ->
                val position = v.tag as OrderHistoryItem
                listener.setOnItemClick(position)

            }

        }
    }

    interface Listener{
        fun setOnItemClick(position: OrderHistoryItem)

    }
}