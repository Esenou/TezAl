package com.example.tezal.ui.home.orgListCategory.badge

import android.app.Activity
import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.view.MenuItem


/**
 * Created by NguyenLuanPC on 19-07-2017.
 */
class NotificationCountSetClass : Activity() {
    companion object {
        private var icon: LayerDrawable? = null

        fun setAddToCart(context: Context, item: MenuItem, numMessages: Int) {
            icon = item.icon as LayerDrawable
            SetNotificationCount.setBadgeCount(context, icon!!, NotificationCountSetClass.setNotifyCount(numMessages))

        }

        fun setNotifyCount(numMessages: Int): Int {
            return numMessages

        }
    }


}//constructor
