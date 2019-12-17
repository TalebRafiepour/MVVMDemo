package com.taleb.mvvmdemo.demo1.data

class AuthenticationManager {

    var nickname: String? = null
    var password: String? = null
    private var userLogged: Boolean = false


    fun logUserIn() {
        userLogged = true
    }


    companion object {

        private var instance: AuthenticationManager? = null

        fun getInstance(): AuthenticationManager {

            synchronized(AuthenticationManager::class.java) {

                if (instance == null)
                    instance = AuthenticationManager()

                return instance!!
            }

        }
    }

}