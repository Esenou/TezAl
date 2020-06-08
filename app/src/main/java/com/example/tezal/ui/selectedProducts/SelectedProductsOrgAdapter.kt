package com.example.tezal.ui.selectedProducts

import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tezal.DBHelper.org.OrgItem
import com.example.tezal.R

class SelectedProductsOrgAdapter(internal var context: Context, internal var orgItems: List<OrgItem>, val listener: Listener): RecyclerView.Adapter<SelectedProductsOrgAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var imgOrg: ImageView
        lateinit var txtName: TextView
        init {
            imgOrg = itemView.findViewById(R.id.imgOrgMark) as ImageView
            txtName = itemView.findViewById(R.id.txtOrgNameMark) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductsOrgAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selectedproducts_org, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orgItems.size
    }

    override fun onBindViewHolder(holder: SelectedProductsOrgAdapter.MyViewHolder, position: Int) {
        holder.txtName.text = StringBuilder(orgItems[position].name).toString()
        Glide.with(holder.itemView).load(orgItems[position].image).into(holder.imgOrg)
      //  holder.itemView.tag = position
        holder.itemView.setOnClickListener { v ->

            listener.setOnItemClick(orgItems[position])

        }

    }

    fun getItemAtPosition(pos: Int): OrgItem {
        return orgItems[pos]
    }

    interface Listener{
        fun setOnItemClick(position: OrgItem)

    }
}