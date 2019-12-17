package com.taleb.mvvmdemo.demo1.features.login


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import com.taleb.mvvmdemo.R
import com.taleb.mvvmdemo.demo1.features.HomeActivity
import com.taleb.mvvmdemo.demo1.base.BaseFragment
import com.taleb.mvvmdemo.demo1.data.AuthenticationManager
import com.taleb.mvvmdemo.demo1.networking.AuthenticationRequestManager
import com.taleb.mvvmdemo.demo1.networking.account.exception.AccountTechFailureException
import com.taleb.mvvmdemo.demo1.networking.games.exception.GamesTechFailureException
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginInternalException
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginTechFailureException
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers



/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var loginSubscription: Subscription? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = LoginViewModel(
            AuthenticationRequestManager.getInstance(activity!!.applicationContext!!))


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(com.taleb.mvvmdemo.R.layout.fragment_login, container, false)
        ButterKnife.bind(this,rootView)
        return rootView
    }

    override fun subscribeForNetworkRequests() {
        loginSubscription = loginViewModel.getLoginSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(LoginSubscriber())

    }

    override fun unsubscribeFromNetworkRequests() {
        loginSubscription?.unsubscribe()
    }

    override fun reconnectWithNetworkRequests() {
        loginSubscription = loginViewModel.createLoginSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(LoginSubscriber())
    }


    @OnClick(R.id.login)
    fun loginButtonTap(view: View) {

        AuthenticationManager.getInstance().password = "password"
        loginViewModel.login()

        progressDialog = ProgressDialog.show(activity, "Login", "...", true)
    }

    private fun showMessage(message: String) {

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchHomeActivity() {

        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }


    private inner class LoginSubscriber: Subscriber<Any>() {
        override fun onNext(t: Any?) {}

        override fun onCompleted() {
            hideProgressDialog()
            launchHomeActivity()
        }

        override fun onError(e: Throwable?) {
            hideProgressDialog();

            // To be able to make another Login request,
            // we have to reset the Subject in the VM
            reconnectWithNetworkRequests();

            if (e is LoginInternalException
                || e is LoginTechFailureException
            ) {

                showMessage("Login Failure");

            } else if (e is AccountTechFailureException) {

                showMessage("Account failed");
                launchHomeActivity();
            } else if (e is GamesTechFailureException) {

                showMessage("Games failed");
                launchHomeActivity();
            }
        }

    }
}


