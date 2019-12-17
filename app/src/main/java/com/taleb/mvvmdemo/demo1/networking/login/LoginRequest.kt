package com.taleb.mvvmdemo.demo1.networking.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("password")
    val password: String
)