package com.taleb.mvvmdemo.demo1.networking.games

import com.taleb.mvvmdemo.demo1.networking.games.exception.GamesTechFailureException
import retrofit.RestAdapter
import rx.Observable

class GamesAPIService(restAdapter: RestAdapter) {

    private val gamesAPI = restAdapter.create(IGamesAPI::class.java)
    var isRequestingGames = false

    fun getGamesInformation(request: GamesRequest): Observable<GamesResponse> =
        gamesAPI.getGamesInformation(request.nickname)
            .doOnSubscribe { isRequestingGames = true }
            .doOnTerminate { isRequestingGames = false }
            .doOnError { handleGamesError(it) }


    private fun handleGamesError(throwable: Throwable): Unit =
        throw GamesTechFailureException()
}