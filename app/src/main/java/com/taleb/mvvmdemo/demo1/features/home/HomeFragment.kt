package com.taleb.mvvmdemo.demo1.features.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.Bind
import butterknife.ButterKnife
import com.taleb.mvvmdemo.demo1.base.BaseFragment
import com.taleb.mvvmdemo.demo1.networking.UserDataRequestManager
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var userDataSubscription: Subscription? = null


    @Bind(com.taleb.mvvmdemo.R.id.user_data)
    private lateinit var userDataText: TextView
    @Bind(com.taleb.mvvmdemo.R.id.swipe_refresh)
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = HomeViewModel(UserDataRequestManager.getInstance(context!!))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(com.taleb.mvvmdemo.R.layout.fragment_home, container, false)
        ButterKnife.bind(this, rootView)
        showRandomSuccessfulMessage()
        setupRefreshLayout()
        return rootView
    }


    override fun subscribeForNetworkRequests() {
        userDataSubscription = homeViewModel.getUserDataSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(UserDataSubscriber())
    }

    override fun reconnectWithNetworkRequests() {
        userDataSubscription = homeViewModel.createUserDataSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(UserDataSubscriber())
    }

    override fun unsubscribeFromNetworkRequests() {
        userDataSubscription?.unsubscribe()
    }


    private fun setupRefreshLayout() =
        swipeRefreshLayout.setOnRefreshListener { homeViewModel.getUserData() }

    private fun showRandomSuccessfulMessage() =
        showMessage(homeViewModel.generateRandomMessage())


    private fun showMessage(message: String) {
        userDataText.text = message
    }


    private fun hideRefreshLayout() {
        swipeRefreshLayout.isRefreshing = false
    }

    private inner class UserDataSubscriber : Subscriber<Any>() {

        override fun onCompleted() {
            hideRefreshLayout()
            reconnectWithNetworkRequests()
        }

        override fun onError(e: Throwable) {
            hideRefreshLayout()
            reconnectWithNetworkRequests()
            showMessage("Error")
        }

        override fun onNext(userDataResponse: Any) {
            showRandomSuccessfulMessage()
        }
    }


}
