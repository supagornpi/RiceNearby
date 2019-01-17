package com.warunya.ricenearby.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.warunya.ricenearby.R
import com.warunya.ricenearby.constant.AppInstance
import com.warunya.ricenearby.firebase.UserManager
import com.warunya.ricenearby.ui.login.LoginActivity
import com.warunya.ricenearby.ui.main.MainActivity
import com.warunya.ricenearby.utils.PermissionUtils

class SplashScreenActivity : AppCompatActivity() {

    private var delayTime: Long = 0
    private var time = 1700L

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setupView()
    }

    @SuppressLint("MissingPermission")
    private fun setupView() {
        handler = Handler()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (PermissionUtils.isRequestPermissionLocation(this)) {
            mFusedLocationClient?.lastLocation!!
                    .addOnSuccessListener(this) { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            AppInstance.getInstance().currentLocation = location
                        }
                    }
            nextActivity()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        nextActivity()
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

    private fun nextActivity() {
        runnable = Runnable {
            finish()
            if (UserManager.isLogin()) {
                MainActivity.start()
            } else {
                LoginActivity.start()
            }
        }
    }
}
