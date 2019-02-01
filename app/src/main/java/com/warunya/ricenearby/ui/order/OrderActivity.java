package com.warunya.ricenearby.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.ui.history.HistoryFragment;

import org.jetbrains.annotations.Nullable;

public class OrderActivity extends AbstractActivity {

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, OrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_order;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        setTitle("ออเดอร์");
        showBackButton();
        replaceFragment();
    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, HistoryFragment.newInstance(true))
                .commit();
    }
}
