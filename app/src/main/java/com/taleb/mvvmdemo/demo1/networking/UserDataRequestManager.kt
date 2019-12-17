package com.taleb.mvvmdemo.demo1.networking

import android.content.Context
import com.taleb.mvvmdemo.demo1.data.DataManager
import com.taleb.mvvmdemo.demo1.model.UserData
import com.taleb.mvvmdemo.demo1.networking.account.AccountAPIService
import com.taleb.mvvmdemo.demo1.networking.account.AccountRequest
import com.taleb.mvvmdemo.demo1.networking.account.AccountResponse
import com.taleb.mvvmdemo.demo1.networking.games.GamesAPIService
import com.taleb.mvvmdemo.demo1.networking.games.GamesRequest
import com.taleb.mvvmdemo.demo1.networking.games.GamesResponse
import com.taleb.mvvmdemo.demo1.networking.mock.RestAdapterFactory
import rx.Observable
import java.util.*

class UserDataRequestManager private constructor(context: Context) {


    private val dataManger: DataManager = DataManager.getInstance()
    private var accountAPIService: AccountAPIService
    private var gamesAPIService: GamesAPIService

    init {
        val restAdapter = RestAdapterFactory.getAdapter(context)
        accountAPIService = AccountAPIService(restAdapter)
        gamesAPIService = GamesAPIService(restAdapter)
    }

    fun getUserData(): Observable<Any> =
        Observable.zip(getAccount(), getGames()) { t, u -> processUserDataResult(t, u) }

    private fun processUserDataResult(
        accountResponse: AccountResponse,
        gamesResponse: GamesResponse
    ): Any {
        val userData = UserData()
        userData.accountInformation = "get information from accountResponse: $accountResponse"
        userData.games = Collections.emptyList() // get information from gamesResponse
        return Observable.just(userData)
    }

    private fun getAccount() = accountAPIService.getAccount(createAccountRequest())

    private fun createAccountRequest() = AccountRequest("nickname")

    private fun getGames() = gamesAPIService.getGamesInformation(createGamesRequest())

    private fun createGamesRequest() = GamesRequest("nickname")

    companion object {
        private var instance: UserDataRequestManager? = null
        fun getInstance(context: Context): UserDataRequestManager =
            synchronized(this) {
                if (instance == null)
                    instance = UserDataRequestManager(context)
                return instance!!
            }
    }
}