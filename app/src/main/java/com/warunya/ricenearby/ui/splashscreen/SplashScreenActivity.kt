package com.warunya.ricenearby.ui.splashscreen

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.warunya.ricenearby.R
import com.warunya.ricenearby.firebase.UserManager
import com.warunya.ricenearby.ui.login.LoginActivity
import com.warunya.ricenearby.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private var delayTime: Long = 0
    private var time = 1700L

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setupView()
    }

    private fun setupView() {
        handler = Handler()

        runnable = Runnable {
            if (UserManager.isLogin()) {
                finish()
                MainActivity.start()
            } else {
                LoginActivity.start()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        delayTime = time
        handler!!.postDelayed(runnable, delayTime)
        time = System.currentTimeMillis()
    }

    public override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(runnable)
        time = delayTime - (System.currentTimeMillis() - time)
    }
}
