package com.yudas1337.recognizeface.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context): LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback
    private var isCallbackRegistered = false

    override fun onActive() {
        super.onActive()
        updateNetworkConnection()
        when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                if (!isCallbackRegistered) {
                    networkConnectionCallback = connectionCallback()
                    connectivityManager.registerDefaultNetworkCallback(networkConnectionCallback)
                    isCallbackRegistered = true
                }
            }
            else -> {
                context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (isCallbackRegistered) {
            connectivityManager.unregisterNetworkCallback(networkConnectionCallback)
            isCallbackRegistered = false
        }
    }
    private fun connectionCallback(): ConnectivityManager.NetworkCallback {
        networkConnectionCallback= object: ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }

        return networkConnectionCallback
    }

    private fun updateNetworkConnection(){
        val networkConnection = connectivityManager.activeNetworkInfo
        postValue(networkConnection?.isConnected == true)
    }

    private val networkReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
           updateNetworkConnection()
        }
    }

    fun clearObserver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkConnectionCallback)
            isCallbackRegistered = false
        } else {
            try {
                context.unregisterReceiver(networkReceiver)
            } catch (e: IllegalArgumentException) {
                e.message?.let { Log.d("obserErr", it) }
            }
        }
    }


}