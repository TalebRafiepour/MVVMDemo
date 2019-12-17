package com.taleb.mvvmdemo.demo1.features.home

import com.taleb.mvvmdemo.demo1.networking.UserDataRequestManager
import rx.subjects.AsyncSubject
import java.util.*

class HomeViewModel(private val userDataRequestManager: UserDataRequestManager) {

    private var userDataSubject: AsyncSubject<Any> = AsyncSubject.create()
    private val random by lazy { Random() }

    fun createUserDataSubject(): AsyncSubject<Any> {
        userDataSubject = AsyncSubject.create()
        return userDataSubject
    }


    fun getUserDataSubject() = userDataSubject

    fun getUserData() = userDataRequestManager.getUserData().subscribe(userDataSubject)

    fun generateRandomMessage(): String {
        val nextRandom = random.nextDouble()
        return nextRandom.toString()
    }


}