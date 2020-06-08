package com.example.tezal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null &&
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) !=null &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            .getState() == NetworkInfo.State.CONNECTED)
            return true;
            else return false;
        }

    }



