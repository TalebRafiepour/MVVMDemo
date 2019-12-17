package com.taleb.mvvmdemo.demo1.data

import com.taleb.mvvmdemo.demo1.model.UserData


class DataManager private constructor(){

    var userData: UserData =
        UserData()


    companion object {
        private var instance: DataManager? = null

        fun getInstance(): DataManager {
            synchronized(DataManager::class.java/*this*/) {
                if (instance == null)
                    instance = DataManager()
                return instance!!
            }
        }
    }
}