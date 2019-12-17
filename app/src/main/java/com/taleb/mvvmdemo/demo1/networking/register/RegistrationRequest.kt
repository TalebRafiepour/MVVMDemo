package com.taleb.mvvmdemo.demo1.networking.register

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("password")
    val password: String
)
