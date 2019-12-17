package com.taleb.mvvmdemo.demo1.networking.register

import retrofit.http.Body
import retrofit.http.POST
import rx.Observable

interface IRegistrationAPI {

    @POST("/registration")
    fun register(@Body request: RegistrationRequest): Observable<RegistrationResponse>
}