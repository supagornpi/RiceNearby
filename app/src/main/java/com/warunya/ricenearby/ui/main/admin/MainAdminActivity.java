package com.warunya.ricenearby.ui.main.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.Tabs;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.FragmentStateManager;
import com.warunya.ricenearby.customs.HolderFragment;

import org.jetbrains.annotations.NotNull;

public class MainAdminActivity extends AbstractActivity {
    private FragmentStateManager fragmentStateManager = null;
    private BottomNavigationViewEx navigation;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, MainAdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_main_admin;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        bindView();

        FrameLayout frameLayout = findViewById(R.id.main_content);
        fragmentStateManager = new FragmentStateManager(frameLayout, getSupportFragmentManager()) {
            @NotNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return HolderFragment.newInstance(Tabs.Seller);
                } else if (position == 1) {
                    return HolderFragment.newInstance(Tabs.Order);
                } else {
                    return HolderFragment.newInstance(Tabs.Seller);
                }
            }
        };

        if (savedInstanceState == null) {
            (fragmentStateManager).changeFragment(0);
        }
//
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int position = getNavPositionFromMenuItem(menuItem);

                if (position != -1) {
                    fragmentStateManager.changeFragment(getNavPositionFromMenuItem(menuItem));
                    return true;
                }

                return false;
            }
        });
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                int position = getNavPositionFromMenuItem(menuItem);
                if (position != -1) {
                    fragmentStateManager.removeFragment(position);
                    fragmentStateManager.changeFragment(position);
                }
            }
        });
//
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
//        navigation.setTextVisibility(false);
//        // set icon size
//        val iconSize = 32f
//        navigation.setIconSize(iconSize, iconSize)
//        // set item height
//        navigation.itemHeight = BottomNavigationViewEx.dp2px(this, (iconSize + 16))
    }

    private void bindView() {
        navigation = findViewById(R.id.navigation);
    }

    private int getNavPositionFromMenuItem(MenuItem menuItem) {
        int position = 0;
        switch (menuItem.getItemId()) {
            case R.id.navigation_seller:
                position = 0;
                break;
            case R.id.navigation_order:
                position = 1;
                break;
        }
        return position;
    }
}
