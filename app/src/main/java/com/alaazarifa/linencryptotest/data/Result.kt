package com.alaazarifa.linencryptotest.data

import com.google.gson.annotations.SerializedName


data class Result(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: String,
)
