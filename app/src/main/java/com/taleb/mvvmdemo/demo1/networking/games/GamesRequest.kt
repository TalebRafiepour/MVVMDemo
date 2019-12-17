package com.taleb.mvvmdemo.demo1.networking.games

import com.google.gson.annotations.SerializedName

data class GamesRequest(
    @SerializedName("nickname")
    val nickname: String
)