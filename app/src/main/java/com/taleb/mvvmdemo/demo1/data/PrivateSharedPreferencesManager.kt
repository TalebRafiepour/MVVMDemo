package com.taleb.mvvmdemo.demo1.data

import android.content.Context
import android.content.SharedPreferences

class PrivateSharedPreferencesManager private constructor(context: Context) {

    private var privateSharedInstance: SharedPreferences

    var userNickname: String
        set(value) {
            storeStringInSharedPreferences(NICKNAME_KEY, value)
        }
        get() = getStringFromSharedPreferences(NICKNAME_KEY)


    init {
        privateSharedInstance = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    }


    private fun storeStringInSharedPreferences(key: String, content: String) {
        val editor = privateSharedInstance.edit()
        editor.putString(key, content)
        editor.apply()
    }

    private fun getStringFromSharedPreferences(key: String) =
        privateSharedInstance.getString(key, "") ?: ""


    companion object {
        private const val PREFERENCES_KEY = "com.taleb.mvvmdemo.demo1"
        private const val NICKNAME_KEY = "nickname"

        private var instance: PrivateSharedPreferencesManager? = null

        fun getInstance(context: Context): PrivateSharedPreferencesManager =
            synchronized(this) {
                if (instance == null)
                    instance = PrivateSharedPreferencesManager(context)
                return instance!!
            }
    }
}