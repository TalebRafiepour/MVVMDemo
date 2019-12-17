package com.taleb.mvvmdemo.demo1.features.registration


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
import com.taleb.mvvmdemo.demo1.base.BaseFragment
import com.taleb.mvvmdemo.demo1.data.AuthenticationManager
import com.taleb.mvvmdemo.demo1.features.HomeActivity
import com.taleb.mvvmdemo.demo1.features.login.LoginFragment
import com.taleb.mvvmdemo.demo1.networking.AuthenticationRequestManager
import com.taleb.mvvmdemo.demo1.networking.account.exception.AccountTechFailureException
import com.taleb.mvvmdemo.demo1.networking.games.exception.GamesTechFailureException
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginInternalException
import com.taleb.mvvmdemo.demo1.networking.login.exception.LoginTechFailureException
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationInternalException
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationNicknameAlreadyExistsException
import com.taleb.mvvmdemo.demo1.networking.register.exception.RegistrationTechFailureException
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers



/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : BaseFragment() {

    private lateinit var registrationViewModel: RegistrationViewModel
    private var registrationSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authenticationRequestManager =
            AuthenticationRequestManager.getInstance(activity!!.applicationContext)
        registrationViewModel = RegistrationViewModel(authenticationRequestManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_registration, container, false)

        ButterKnife.bind(this, rootView)

        return rootView
    }

    override fun subscribeForNetworkRequests() {

        registrationSubscription = registrationViewModel.registrationSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RegistrationSubscriber())
    }

    override fun reconnectWithNetworkRequests() {

        registrationSubscription = registrationViewModel.createRegistrationSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RegistrationSubscriber())
    }

    override fun unsubscribeFromNetworkRequests() {

        if (registrationSubscription != null) {
            registrationSubscription!!.unsubscribe()
        }
    }

    @OnClick(com.taleb.mvvmdemo.R.id.register)
    fun registerButtonTap(view: View) {

        AuthenticationManager.getInstance().nickname = "nickname"
        AuthenticationManager.getInstance().password =  "password"
        registrationViewModel.register()

        progressDialog = ProgressDialog.show(activity, "Registering", "...", true)
    }

    private fun showMessage(message: String) {

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessageAndGoToLogin(message: String) {

        showMessage(message)

        activity!!.title = "Login"
        activity!!.supportFragmentManager.beginTransaction()
            .replace(com.taleb.mvvmdemo.R.id.fragment_container, LoginFragment()).commit()
    }

    private fun launchHomeActivity() {

        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    private inner class RegistrationSubscriber : Subscriber<Any>() {

        override fun onCompleted() {

            hideProgressDialog()
            launchHomeActivity()
        }

        override fun onError(e: Throwable) {

            hideProgressDialog()

            // To be able to make another Registration request,
            // we have to reset the Subject in the VM
            reconnectWithNetworkRequests()

            if (e is RegistrationInternalException || e is RegistrationTechFailureException) {

                showMessage("Registration Failure")

            } else if (e is RegistrationNicknameAlreadyExistsException) {

                showMessage("Registration Nickname already exists")

            } else if (e is LoginInternalException || e is LoginTechFailureException) {

                showMessageAndGoToLogin("Login Failure")

            } else if (e is AccountTechFailureException) {

                showMessageAndGoToLogin("Account failed")
            } else if (e is GamesTechFailureException) {

                showMessageAndGoToLogin("Games failed")
            }
        }

        override fun onNext(getUserDataResponse: Any) {

        }
    }
}
