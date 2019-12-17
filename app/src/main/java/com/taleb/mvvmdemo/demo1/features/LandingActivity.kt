package com.taleb.mvvmdemo.demo1.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.Bind
import butterknife.ButterKnife
import com.taleb.mvvmdemo.R
import com.taleb.mvvmdemo.demo1.data.PrivateSharedPreferencesManager
import com.taleb.mvvmdemo.demo1.features.login.LoginFragment
import com.taleb.mvvmdemo.demo1.features.registration.RegistrationFragment

class LandingActivity : AppCompatActivity() {

    @Bind(R.id.toolBar)
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        if (savedInstanceState != null)
            return

        val userNickname = PrivateSharedPreferencesManager.getInstance(this).userNickname
        val initialFragment: Fragment
        if (userNickname.isNullOrEmpty()) {
            title = "Registeration"
            initialFragment = RegistrationFragment()
        } else {
            title = "Login"
            initialFragment = LoginFragment()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, initialFragment).commit();

    }
}
