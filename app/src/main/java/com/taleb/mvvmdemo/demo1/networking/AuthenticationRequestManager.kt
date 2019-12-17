package com.taleb.mvvmdemo.demo1.networking

import android.content.Context
import com.taleb.mvvmdemo.demo1.data.AuthenticationManager
import com.taleb.mvvmdemo.demo1.data.PrivateSharedPreferencesManager
import com.taleb.mvvmdemo.demo1.networking.login.LoginApiService
import com.taleb.mvvmdemo.demo1.networking.login.LoginRequest
import com.taleb.mvvmdemo.demo1.networking.login.LoginResponse
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginInternalException
import com.taleb.mvvmdemo.demo1.networking.mock.RestAdapterFactory
import com.taleb.mvvmdemo.demo1.networking.register.RegistrationAPIService
import com.taleb.mvvmdemo.demo1.networking.register.RegistrationRequest
import com.taleb.mvvmdemo.demo1.networking.register.RegistrationResponse
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationInternalException
import retrofit.RestAdapter
import rx.Observable



class AuthenticationRequestManager private constructor(context: Context) {

    private val authenticationManager by lazy { AuthenticationManager.getInstance() }

    private val privateSharedPreferencesManager by lazy {
        PrivateSharedPreferencesManager.getInstance(context)
    }

    private val restAdapter: RestAdapter by lazy {
        RestAdapterFactory.getAdapter(context)
    }

    private val loginAPIService: LoginApiService by lazy {
        LoginApiService(restAdapter, authenticationManager)
    }

    private val registrationAPIService by lazy {
        RegistrationAPIService(restAdapter, privateSharedPreferencesManager)
    }

    private val userDataRequestManager by lazy {
        UserDataRequestManager.getInstance(context)
    }

    fun register(): Observable<Any> =
        registrationAPIService.register(createRegistrationRequest())
            .flatMap { makeLoginRequest(it) }

    fun login(): Observable<Any> =
        loginAPIService.login(createLoginRequest())
            .flatMap { makeGetUserData(it) }

    private fun makeGetUserData(loginResponse: LoginResponse): Observable<Any> =
        userDataRequestManager.getUserData()

    private fun makeLoginRequest(registrationResponse: RegistrationResponse)
            :Observable<Any> =
        login()


    private fun createRegistrationRequest(): RegistrationRequest {
        val nickname = authenticationManager.nickname
        val password = authenticationManager.password

        if (nickname.isNullOrEmpty() || password.isNullOrEmpty())
            throw RegistrationInternalException()
        return RegistrationRequest(nickname, password)
    }

    private fun createLoginRequest(): LoginRequest {

        val nickname = privateSharedPreferencesManager.userNickname
        val password = authenticationManager.password

        if (nickname.isNullOrEmpty() || password.isNullOrEmpty()) {
            throw LoginInternalException()
        }

        return LoginRequest(nickname, password)
    }


    companion object {
        private var instance: AuthenticationRequestManager? = null

        fun getInstance(context: Context): AuthenticationRequestManager =
            synchronized(this) {
                if (instance == null)
                    instance = AuthenticationRequestManager(context)
                return instance!!
            }
    }
}