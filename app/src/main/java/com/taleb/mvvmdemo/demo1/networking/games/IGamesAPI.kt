package com.taleb.mvvmdemo.demo1.networking.games

import retrofit.http.GET
import retrofit.http.Header
import rx.Observable

interface IGamesAPI {

    @GET("/games")
    fun getGamesInformation(@Header("nickname") nickname: String): Observable<GamesResponse>
}