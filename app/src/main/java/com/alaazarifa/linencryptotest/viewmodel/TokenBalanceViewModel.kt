package com.alaazarifa.linencryptotest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alaazarifa.linencryptotest.data.Result
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import org.json.JSONObject


class TokenBalanceViewModel : ViewModel() {

    val balance: MutableStateFlow<String?> = MutableStateFlow(null)

    private val contractAddress = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"  // USDC Token Contract
    private val address = "0x7DBB4bdCfE614398D1a68ecc219F15280d0959E0" // Target Address
    private val url = "https://api.etherscan.io/api?/" // API Endpoint

    // API params
    private val params: List<Pair<String, Any?>> =
        listOf(
            Pair("apikey", "AJZ7ED5UAGQS4C6FCSFCDIY1NV62RC4A65"),
            Pair("contractaddress", contractAddress),
            Pair("address", address),
            Pair("module", "account"),
            Pair("action", "tokenbalance"),
            Pair("tag", "latest")
        )




    fun fetchTokenBalance() {

        AndroidNetworking.get(url)
            .addQueryParameter(params.toMap())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val data = Gson().fromJson(response.toString(), Result::class.java)
                    balance.value = data.result
                }
                override fun onError(e: ANError?) {
                    e?.printStackTrace()
                }
            })
    }
}
