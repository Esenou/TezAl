package com.example.tezal.ui.share

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyz.widget.PullRefreshLayout
import com.example.tezal.R
import com.example.tezal.model.OrderHistoryItem
import com.example.tezal.ui.home.orgListCategory.orgListCategoryActivity
import com.example.tezal.ui.share.details.HistoryActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog_registration.*
import kotlinx.android.synthetic.main.fragment_share.*

class HistoryFragment : Fragment(),HistoryContract.View,HistoryAdapter.Listener {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var presenter: HistoryPresenter
    lateinit var adapter: HistoryAdapter
    var getId:Long = 0
    var swipeHistory: PullRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_share, container, false)
        swipeHistory = root.findViewById(R.id.swipeHistory)
        swipeHistory?.setOnRefreshListener {
              // Do work to refresh the list here.
              init()
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.activity!!.getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)
        getId = sharedPreferences?.getLong("clientId",0)
        init()
    }

    fun init(){
        presenter = HistoryPresenter(this)
        presenter.getOrganization(getId)
    }

    override fun onSuccssesGetHistory(list: List<OrderHistoryItem>) {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistory?.layoutManager = GridLayoutManager(activity, 1)
        recyclerViewHistory?.layoutManager = layoutManager
        adapter = HistoryAdapter(list, this)
        recyclerViewHistory.adapter = adapter
    }

    override fun onFailure() {
        Toast.makeText(activity,"Error", Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        swipeHistory?.setRefreshing(true)
    }

    override fun hideProgress() {
        swipeHistory?.setRefreshing(false)
    }

    override fun setOnItemClick(position: OrderHistoryItem) {
        val intent = Intent(activity, HistoryActivity::class.java)
        val bundle = Bundle()
        bundle.putLong("key", position.id)
        intent.putExtras(bundle)
        startActivity(intent)
        Toast.makeText(context!!,"item :"+position.id,Toast.LENGTH_SHORT).show()
    }
}