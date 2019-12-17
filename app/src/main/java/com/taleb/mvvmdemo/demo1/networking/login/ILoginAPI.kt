package com.taleb.mvvmdemo.demo1.networking.login

import retrofit.client.Response
import retrofit.http.GET
import retrofit.http.Header
import rx.Observable

interface ILoginAPI {

    @GET("/login")
    fun login(@Header("nickname") nickname: String,
              @Header("password") password: String): Observable<Response>
}