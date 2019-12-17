package com.taleb.mvvmdemo.demo1.networking.account

import retrofit.http.GET
import retrofit.http.Header
import rx.Observable

interface IAccountAPI {

    @GET("/account")
    fun getAccountInformation(@Header("nickname") nickname: String): Observable<AccountResponse>
}