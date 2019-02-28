package com.warunya.ricenearby.ui.splashscreen;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.ui.login.LoginActivity;
import com.warunya.ricenearby.ui.main.MainActivity;
import com.warunya.ricenearby.utils.PermissionUtils;

public class SplashScreenActivity extends AppCompatActivity {

    private long delayTime = 0L;
    private long time = 1700L;
    private Handler handler;
    private Runnable runnable;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setupView();
    }

    @SuppressLint("MissingPermission")
    private void setupView() {
        handler = new Handler();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (PermissionUtils.Companion.isRequestPermissionLocation(this)) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        AppInstance.getInstance().setCurrentLocation(location);
                    }
                }

            });
            nextActivity();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nextActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        delayTime = time;
        handler.postDelayed(runnable, delayTime);
        time = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delayTime - (System.currentTimeMillis() - time);
    }

    private void nextActivity() {
        runnable = new Runnable() {
            @Override
            public void run() {
                finish();
                if (UserManager.isLogin()) {
                    MainActivity.start();
                } else {
                    LoginActivity.start();
                }
            }
        };
    }
}
