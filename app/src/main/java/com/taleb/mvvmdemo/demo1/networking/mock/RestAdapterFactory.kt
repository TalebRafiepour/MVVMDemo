package com.taleb.mvvmdemo.demo1.networking.mock

import android.content.Context
import retrofit.RestAdapter


object RestAdapterFactory {

    fun getAdapter(context: Context): RestAdapter = createAdapter(context)

    private fun createAdapter(context: Context): RestAdapter =
        RestAdapter.Builder()
            .setClient(MockHttpClient(context))
            .setEndpoint("mock")
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()

}