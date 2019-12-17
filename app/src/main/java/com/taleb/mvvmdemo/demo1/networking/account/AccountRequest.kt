package com.taleb.mvvmdemo.demo1.networking.account

import com.google.gson.annotations.SerializedName

data class AccountRequest(
    @SerializedName("nickname")
    val nickname: String
)