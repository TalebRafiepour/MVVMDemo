package com.taleb.mvvmdemo.demo1.features.registration

import com.taleb.mvvmdemo.demo1.networking.AuthenticationRequestManager
import rx.subjects.AsyncSubject


class RegistrationViewModel(private val authenticationRequestManager: AuthenticationRequestManager) {

    var registrationSubject: AsyncSubject<Any> = AsyncSubject.create()
        private set


    fun createRegistrationSubject(): AsyncSubject<Any> {

        registrationSubject = AsyncSubject.create()
        return registrationSubject
    }

    fun register() {

        authenticationRequestManager.register()
            .subscribe(registrationSubject)
    }
}