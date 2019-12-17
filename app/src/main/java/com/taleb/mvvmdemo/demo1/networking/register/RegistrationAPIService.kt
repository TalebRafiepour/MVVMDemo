package com.taleb.mvvmdemo.demo1.networking.register

import com.taleb.mvvmdemo.demo1.data.PrivateSharedPreferencesManager
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationNicknameAlreadyExistsException
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationTechFailureException
import retrofit.RestAdapter
import retrofit.RetrofitError
import rx.Observable


class RegistrationAPIService(
    restAdapter: RestAdapter,
    private val privateSharedPreferencesManager: PrivateSharedPreferencesManager
) {

    var registrationAPI: IRegistrationAPI = restAdapter.create(IRegistrationAPI::class.java)
    var isRequestingRegistration = false


    fun register(request: RegistrationRequest): Observable<RegistrationResponse> =
        registrationAPI.register(request)
            .doOnSubscribe { isRequestingRegistration = true }
            .doOnTerminate { isRequestingRegistration = false }
            .doOnError { handleRegistrationError(it) }
            .doOnNext { processRegistrationResponse(request, it) }


    private fun handleRegistrationError(throwable: Throwable) {

        if (throwable is RetrofitError) {

            val status = throwable.response.status

            if (status == 401) {
                throw RegistrationNicknameAlreadyExistsException()
            } else {
                throw RegistrationTechFailureException()
            }

        } else {
            throw RegistrationTechFailureException()
        }
    }

    private fun processRegistrationResponse(
        registrationRequest: RegistrationRequest,
        registrationResponse: RegistrationResponse
    ) {
        privateSharedPreferencesManager.userNickname = registrationRequest.nickname
    }


}