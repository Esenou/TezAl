package com.example.tezal.ui.selectedProducts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tezal.Callback.IMyButtonCallback
import com.example.tezal.DBHelper.CartDatabase
import com.example.tezal.DBHelper.org.LocalOrgDataSource
import com.example.tezal.DBHelper.org.OrgDataSource
import com.example.tezal.DBHelper.org.OrgItem
import com.example.tezal.R
import com.example.tezal.common.MySwipeHelper
import com.example.tezal.ui.home.orgListCategory.rawMaterial.rawActivity
import com.google.gson.Gson
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SelectedProductsFragment : Fragment(),SelectedProductsOrgAdapter.Listener {

    var adapterSelectedProducts:SelectedProductsOrgAdapter?=null
    private var orgDataSource: OrgDataSource?=null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var recyclerViewState: Parcelable?= null
    private lateinit var selectedProductsViewModel: SelectedProductsViewModel
    var txtEmptyOrg:TextView? = null
    var txtName:TextView? = null
    var recyclerViewOrgMark: RecyclerView? = null

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = this.getActivity()!!.getSharedPreferences("SP_INFO_CLIENT", Context.MODE_PRIVATE)
        selectedProductsViewModel =  ViewModelProviders.of(this).get(SelectedProductsViewModel::class.java)
        selectedProductsViewModel.initCartDataSource(context!!)
        val root = inflater.inflate(R.layout.fragment_selectedproducts, container, false)
        selectedProductsViewModel.getMutableLiveDataCartItem().observe(this, Observer {
            if (it == null || it.isEmpty()){
                recyclerViewOrgMark!!.visibility = View.GONE
                txtEmptyOrg!!.visibility = View.VISIBLE

            } else {
                recyclerViewOrgMark!!.visibility = View.VISIBLE
                txtEmptyOrg!!.visibility = View.GONE
                adapterSelectedProducts = SelectedProductsOrgAdapter(context!!,it,this)
                recyclerViewOrgMark!!.adapter = adapterSelectedProducts
            }
        })

        initViews(root)
        return root
    }
    private fun initViews(root:View) {
        setHasOptionsMenu(true)
        orgDataSource = LocalOrgDataSource(CartDatabase.getInstance(context!!).OrgDao())

        recyclerViewOrgMark = root.findViewById(R.id.recyclerViewOrgMark) as RecyclerView
        recyclerViewOrgMark!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerViewOrgMark!!.layoutManager = layoutManager
        recyclerViewOrgMark!!.addItemDecoration(DividerItemDecoration(context,layoutManager.orientation))
        val swipe = object : MySwipeHelper(context!!,recyclerViewOrgMark!!,200)
        {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton(context!!,
                    "Delete",
                    30,
                    0,
                    Color.parseColor("#FF3c30"),
                    object : IMyButtonCallback {
                        override fun onClick(pos: Int) {

                            val deleteItem = adapterSelectedProducts!!.getItemAtPosition(pos)
                            orgDataSource!!.deleteOrg(deleteItem)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : SingleObserver<Int> {
                                    override fun onSuccess(t: Int) {
                                        adapterSelectedProducts!!.notifyItemRemoved(pos)
                                        Toast.makeText(context,"Delete item success",Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                    }

                                    override fun onError(e: Throwable) {
                                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                                    }
                                })
                            println("Delete -------------------->")
                            Toast.makeText(context,"Delete Item",Toast.LENGTH_SHORT).show()
                        }
                    }))
            }
        }
        txtEmptyOrg = root.findViewById(R.id.txtEmptyOrg) as? TextView
    }

    override fun setOnItemClick(position: OrgItem) {
        val editor = sharedPreferences.edit()
        editor.putInt("organizationId", position.id.toInt())
        editor.apply()
        Toast.makeText(context!!, "Item :" + position.id, Toast.LENGTH_LONG).show()
        val intent = Intent(context!!, rawActivity::class.java)
        val bundle = Bundle()
        bundle.putString("key", Gson().toJson(position))
        intent.putExtras(bundle)
        startActivity(intent)
    }
}