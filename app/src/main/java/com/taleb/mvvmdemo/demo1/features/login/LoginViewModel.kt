package com.taleb.mvvmdemo.demo1.features.login

import com.taleb.mvvmdemo.demo1.networking.AuthenticationRequestManager
import rx.Subscription
import rx.subjects.AsyncSubject

class LoginViewModel(private val authenticationRequestManager: AuthenticationRequestManager) {

    private var loginSubject: AsyncSubject<Any> = AsyncSubject.create()

    fun createLoginSubject(): AsyncSubject<Any> {
        loginSubject = AsyncSubject.create()
        return loginSubject
    }

    fun getLoginSubject(): AsyncSubject<Any> = loginSubject

    fun login():Subscription = authenticationRequestManager.login().subscribe(loginSubject)

}