package com.lossabinos.data.network


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(checkConnection())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d("NET_MONITOR", "🟢 onAvailable")
            _isOnline.value = checkConnection()
        }

        override fun onLost(network: Network) {
            Log.d("NET_MONITOR", "🔴 onLost")
            _isOnline.value = checkConnection()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            caps: NetworkCapabilities
        ) {
            Log.d(
                "NET_MONITOR",
                "🔁 onCapabilitiesChanged → " +
                        "INTERNET=${caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)} " +
                        "VALIDATED=${caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)} " +
                        "TRANSPORT_WIFI=${caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)} " +
                        "TRANSPORT_CELL=${caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}"
            )

            _isOnline.value = hasInternet(caps)
        }
    }

    init {
        Log.d("NET_MONITOR", "🚀 NetworkMonitor INIT ${hashCode()}")
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    private fun checkConnection(): Boolean {
        val network = connectivityManager.activeNetwork
        if (network == null) {
            Log.d("NET_MONITOR", "❌ activeNetwork = null")
            return false
        }

        val caps = connectivityManager.getNetworkCapabilities(network)
        if (caps == null) {
            Log.d("NET_MONITOR", "❌ caps = null")
            return false
        }

        val result = hasInternet(caps)
        Log.d("NET_MONITOR", "🔎 checkConnection = $result")
        return result
    }

    private fun hasInternet(caps: NetworkCapabilities): Boolean {
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

/*
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isOnline = MutableStateFlow(checkConnection())
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d("NET_MONITOR", "🟢 onAvailable")
            _isOnline.value = checkConnection()
        }

        override fun onLost(network: Network) {
            Log.d("NET_MONITOR", "🔴 onLost")
            _isOnline.value = checkConnection()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            Log.d(
                "NET_MONITOR",
                "🔁 onCapabilitiesChanged → " +
                        "INTERNET=${networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)} " +
                        "VALIDATED=${networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)} " +
                        "TRANSPORT_WIFI=${networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)} " +
                        "TRANSPORT_CELL=${networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}"
            )

            _isOnline.value = hasInternet(networkCapabilities)
        }
    }

    init {
        Log.d("NET_MONITOR", "🚀 NetworkMonitor INIT ${hashCode()}")
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    private fun checkConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        return hasInternet(caps)
    }

    private fun hasInternet(caps: NetworkCapabilities): Boolean {
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

 */

    /*
        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val _isOnline = MutableStateFlow(checkInitialConnection())
        val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

        private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline.value = true
            }

            override fun onLost(network: Network) {
                _isOnline.value = checkInitialConnection()
            }
        }

        init {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            //connectivityManager.registerNetworkCallback(request, callback)
            connectivityManager.registerDefaultNetworkCallback( callback)
        }

        private fun checkInitialConnection(): Boolean {
            val network = connectivityManager.activeNetwork ?: return false
            val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
            return caps.hasCapability(NET_CAPABILITY_INTERNET) &&
                    caps.hasCapability(NET_CAPABILITY_VALIDATED)
        }
     */
}
