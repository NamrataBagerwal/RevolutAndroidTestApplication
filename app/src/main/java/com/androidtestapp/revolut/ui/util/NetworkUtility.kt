package com.androidtestapp.revolut.ui.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtility {

    fun isNetworkAvailable(context: Context): Boolean?{
        val connectivityManager: ConnectivityManager = context.getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities: NetworkCapabilities? =  connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo.isConnectedOrConnecting
        }

    }
}