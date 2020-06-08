package com.example.tezal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tezal.model.OrgCategoryItem
import com.example.tezal.model.ProdCatergoryItem
import com.example.tezal.ui.home.HomeAdapter
import com.example.tezal.ui.home.HomeContract
import com.example.tezal.ui.home.HomePresenter
import com.example.tezal.ui.home.orgListCategory.orgListCategoryActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(),HomeContract.View,HomeAdapter.Listener {


    lateinit var presenter: HomePresenter
    lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        init()

       /* cardViewClient.setOnClickListener {
            val intent= Intent(activity,ClientActivity::class.java)
            startActivity(intent)
        }
        cardViewBlog.setOnClickListener {
            val intent=Intent(activity,BlogActivity::class.java)
            startActivity(intent)
        }*/

    }

    override fun onSuccssesGetCategory(list: List<OrgCategoryItem>) {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewCategory?.layoutManager = GridLayoutManager(activity, 1)
        recyclerViewCategory?.layoutManager = layoutManager
        adapter = HomeAdapter(list, this)
        recyclerViewCategory.adapter = adapter

    }
    override fun onFailure() {

    }

    override fun showProgress() {
      //  blogActivitySwipe.setRefreshing(true)
       // ProgressBar.isRefreshing = true
        //main_swip.setRefreshing(true)
    }

    override fun hideProgress() {

    }
    fun init() {
        presenter = HomePresenter(this)
        presenter.getCategory()
    }



    override fun setOnItemClick(position: OrgCategoryItem) {
        Toast.makeText(activity, "Item :" + position.id, Toast.LENGTH_LONG).show()

        val intent = Intent(activity, orgListCategoryActivity::class.java)
        val bundle = Bundle()
        bundle.putString("key", Gson().toJson(position))
        intent.putExtras(bundle)
        startActivity(intent)
    }
}