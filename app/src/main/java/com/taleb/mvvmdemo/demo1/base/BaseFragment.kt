package com.taleb.mvvmdemo.demo1.base

import android.app.ProgressDialog
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    protected var progressDialog:ProgressDialog? = null


    override fun onResume() {
        super.onResume()
        subscribeForNetworkRequests()
    }


    override fun onPause() {
        super.onPause()
        unsubscribeFromNetworkRequests()
    }





    protected fun hideProgressDialog() {
        progressDialog?.let {
            if (it.isShowing)
                it.dismiss()
        }
    }

    // abstract methods
    protected abstract fun subscribeForNetworkRequests();
    protected abstract fun unsubscribeFromNetworkRequests();
    protected abstract fun reconnectWithNetworkRequests();


}