package com.example.tezal

import android.app.Service
import androidx.core.app.NotificationCompat.getCategory
import com.example.tezal.data.ForumService
import com.example.tezal.data.Network
import com.example.tezal.model.AuthModel
import com.example.tezal.model.Order
import com.example.tezal.model.OrgCategoryItem
import com.example.tezal.ui.authorization.LoginContract
import com.example.tezal.ui.authorization.LoginPresenter
import com.example.tezal.ui.home.HomePresenter
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestTest {
   // var startApplication: StartApplication? = null
    lateinit var service: ForumService
    private val BASE_URL="https://tezal.herokuapp.com"
    var json : AuthModel? = null
    var order: Order? = null
    @Before
    fun setUp(){
        service = Network.initService(BASE_URL)
        json = AuthModel("3","3")
        order = Order(7,"AWAITING",3)
    }

    @Test
    fun testGetCategory() {
        val response = service.getCategory().execute()
        assertEquals(response.code(),200)
    }

    @Test
    fun testPostAuthorization() {
        val response = service.setDate(json!!).execute()
        println("response" + response)
        assertEquals(response.code(),202)
    }

    @Test
    fun testGetOranization(){
        val response = service.getOrgListCategory(3).execute()
        assertEquals(response.code(),200)
    }

    @Test
    fun testGetRaw(){
        val response = service.getRawList(3).execute()
        assertEquals(response.code(), 200)
    }

    @Test
    fun testPostOrder(){
        val response = service.setDate(order!!).execute()
        assertEquals(response.code(), 200)
    }

}