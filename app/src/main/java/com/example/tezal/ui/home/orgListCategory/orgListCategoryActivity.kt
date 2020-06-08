package com.example.tezal.ui.home.orgListCategory

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tezal.R
import com.example.tezal.model.OrgCategoryItem
import com.example.tezal.model.OrgListCategoryItem
import com.example.tezal.ui.home.orgListCategory.rawMaterial.rawActivity
import com.google.gson.Gson
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.activity_org_list_category.*
import java.util.*
import kotlin.collections.ArrayList


class orgListCategoryActivity : AppCompatActivity(),orgListCategoryContract.View, orgListCategoryAdapter.Listener {

    lateinit var presenter: orgListCategoryPresenter
    lateinit var adapter: orgListCategoryAdapter
    lateinit var sharedPreferences: SharedPreferences
    var displayList: ArrayList<OrgListCategoryItem>? = null
    var arrayList: ArrayList<OrgListCategoryItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_org_list_category)
        sharedPreferences = getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)
       val bundle = intent.extras
       if (bundle != null) {
            val json = bundle.getString("key")
            val vh = Gson().fromJson<OrgCategoryItem>(json, OrgCategoryItem::class.java!!)
            init(vh.id)
            println("-----------------------------------------------------")
            println("orgCategory -->" + vh.id)
            println("-----------------------------------------------------")
           // Toast.makeText(this, "Item :" + bundle.toString(), Toast.LENGTH_LONG).show()
        }



    }
    private fun init(id:Int){
        presenter = orgListCategoryPresenter(this)
        presenter.getOrgListCategory(id)
    }

    override fun onSuccssesGetOrgListCategory(list: List<OrgListCategoryItem>) {
        displayList = ArrayList<OrgListCategoryItem>()
        arrayList = ArrayList<OrgListCategoryItem>()
        arrayList?.addAll(list)
        displayList?.addAll(arrayList!!)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewOrgListCategory?.layoutManager = GridLayoutManager(this, 1)
        recyclerViewOrgListCategory?.layoutManager = layoutManager
        adapter = orgListCategoryAdapter(displayList!!, this,this)
        recyclerViewOrgListCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val item:MenuItem = menu!!.findItem(R.id.action_search)
        if(item != null) {
            val searchView = item.actionView as SearchView
             // val editText = searchView.findViewById<EditText>(R.id.search_src_text)
             // editText.hint = "Search..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    println("Hello searching! " + newText)
                    if (newText!!.isEmpty()) {
                        displayList?.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arrayList?.forEach {
                            if (it.categoryName.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList?.add(it)
                            }
                        }
                        recyclerViewOrgListCategory.adapter!!.notifyDataSetChanged()
                    } else {
                        displayList?.clear()
                        displayList?.addAll(arrayList!!)
                        recyclerViewOrgListCategory.adapter!!.notifyDataSetChanged()

                    }
                    return true
                }

            })


        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onFailure() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setOnItemClick(position: OrgListCategoryItem) {
        val editor = sharedPreferences.edit()
        editor.putInt("organizationId", position.id)
        editor.apply()
        Toast.makeText(this, "Item :" + position.id, Toast.LENGTH_LONG).show()
        val intent = Intent(this, rawActivity::class.java)
        val bundle = Bundle()
        bundle.putString("key", Gson().toJson(position))
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
