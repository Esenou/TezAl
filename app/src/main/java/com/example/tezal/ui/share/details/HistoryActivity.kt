package com.example.tezal.ui.share.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tezal.R
import com.example.tezal.model.RawHistory
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(),HistoryDetailContract.View {

    lateinit var detailPresenter: HistoryDetailPresenter
    lateinit var detailAdapter: HistoryDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val bundle = intent.extras
        if (bundle != null) {
            val getId = bundle.getLong("key")

            init(getId)
            println("-----------------------------------------------------")
            println("orgCategory -->" + getId)
            println("-----------------------------------------------------")
        }
    }
    fun init(id:Long){
        detailPresenter = HistoryDetailPresenter(this)
        detailPresenter.getRaw(id)
    }

    override fun onSuccssesGetHistory(list: RawHistory) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewHistoryDetails?.layoutManager = GridLayoutManager(this, 1)
        recyclerViewHistoryDetails?.layoutManager = layoutManager
        detailAdapter = HistoryDetailAdapter(list)
        recyclerViewHistoryDetails.adapter = detailAdapter
    }

    override fun onFailure() {

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }
}
