package com.ryanconway.spaceflightnews.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.ryanconway.spaceflightnews.domain.repository.ConnectivityRepository

class ConnectivityRepositoryImpl(
    context: Context
): ConnectivityRepository {

    private val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnectedToNetwork(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            isConnected()
        } else {
            isConnectedApi23()
        }
    }

    @Suppress("DEPRECATION")
    private fun isConnected(): Boolean {
        val activeNetwork: NetworkInfo? = manager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnectedApi23(): Boolean {
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        return when {
            capabilities == null -> false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            else -> false
        }
    }
}