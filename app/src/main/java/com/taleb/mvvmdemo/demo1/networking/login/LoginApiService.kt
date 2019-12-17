package com.taleb.mvvmdemo.demo1.networking.login

import com.google.gson.Gson
import com.taleb.mvvmdemo.demo1.data.AuthenticationManager
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginTechFailureException
import retrofit.RestAdapter
import retrofit.client.Response
import rx.Observable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class LoginApiService(
    restAdapter: RestAdapter,
    private val authenticationManager: AuthenticationManager
) {

    private var loginApi: ILoginAPI = restAdapter.create(ILoginAPI::class.java)
    var isRequestingLogin = false


    fun login(loginRequest: LoginRequest): Observable<LoginResponse> =
        loginApi.login(loginRequest.nickname, loginRequest.password)
            .doOnSubscribe { isRequestingLogin = true }
            .doOnTerminate { isRequestingLogin = false }
            .doOnError { this.handleLoginError(it) }
            .flatMap { parseLoginResponse(it) }
            .doOnNext { processLoginResponse(it) }


    private fun handleLoginError(throwable: Throwable): Unit = throw LoginTechFailureException()

    private fun processLoginResponse(loginResponse: LoginResponse) = authenticationManager.logUserIn()

    private fun parseLoginResponse(response: Response): Observable<LoginResponse>? =
        try {
            val body = fromStream(response.body.`in`())

            val loginResponse = Gson().fromJson<LoginResponse>(body, LoginResponse::class.java)
            loginResponse.loginStatusResponse = response.status
            Observable.just(loginResponse)

        } catch (e: IOException) {
            null
        }


    @Throws(IOException::class)
    private fun fromStream(inputStream: InputStream): String {

        val reader = BufferedReader(InputStreamReader(inputStream))
        val out = StringBuilder()
        val newLine = System.getProperty("line.separator")

        var line: String? = reader.readLine()
        while (line != null) {
            out.append(line)
            out.append(newLine)

            line = reader.readLine()
        }

        return out.toString()
    }

}