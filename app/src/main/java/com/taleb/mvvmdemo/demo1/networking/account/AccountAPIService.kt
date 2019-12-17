package com.taleb.mvvmdemo.demo1.networking.account

import com.taleb.mvvmdemo.demo1.networking.account.exception.AccountTechFailureException
import retrofit.RestAdapter
import rx.Observable


class AccountAPIService(restAdapter: RestAdapter) {

    private val accountAPI = restAdapter.create(IAccountAPI::class.java)
    var isRequestingAccount = false

    fun getAccount(request: AccountRequest): Observable<AccountResponse> =
        accountAPI.getAccountInformation(request.nickname)
            .doOnSubscribe { isRequestingAccount = true }
            .doOnTerminate { isRequestingAccount = false }
            .doOnError { handleAccountError(it) }

    private fun handleAccountError(throwable: Throwable): Unit =
        throw AccountTechFailureException()

}