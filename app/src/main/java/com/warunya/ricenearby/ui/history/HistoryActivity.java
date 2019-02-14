package com.warunya.ricenearby.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.ui.cart.CartFragment;

import org.jetbrains.annotations.Nullable;

public class HistoryActivity extends AbstractActivity {

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_cart;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        setTitle("ประวัติการสั่งซื้อ");
        showBackButton();
        replaceFragment();
    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container,  HistoryFragment.newInstance(false))
                .commit();

    }

}
