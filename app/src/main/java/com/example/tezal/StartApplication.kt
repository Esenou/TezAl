package com.example.tezal

import android.app.Application
import com.example.tezal.data.ForumService
import com.example.tezal.data.Network


class StartApplication:Application() {
    private val BASE_URL="https://tezal.herokuapp.com"

    companion object {
        @Volatile
        lateinit var service: ForumService

    }

    override fun onCreate() {
        super.onCreate()
        service = Network.initService(BASE_URL)
    }
}