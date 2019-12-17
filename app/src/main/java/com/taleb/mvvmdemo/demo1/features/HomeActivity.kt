package com.taleb.mvvmdemo.demo1.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.Bind
import butterknife.ButterKnife
import com.taleb.mvvmdemo.R
import com.taleb.mvvmdemo.demo1.features.home.HomeFragment

class HomeActivity : AppCompatActivity() {

    @Bind(R.id.toolBar)
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        if (savedInstanceState != null)
            return

        title = "Home"
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, HomeFragment())
            .commit()
    }
}
