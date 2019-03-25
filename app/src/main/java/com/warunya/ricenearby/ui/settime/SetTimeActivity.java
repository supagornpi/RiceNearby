package com.warunya.ricenearby.ui.settime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.ui.settime.food.SetTimeFoodActivity;

public class SetTimeActivity extends AbstractActivity {


    private Button btnAddBreakfast;
    private Button btnAddLunch;
    private Button btnAddDinner;
    private Button btnAddLateDinner;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, SetTimeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_set_time;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle(R.string.title_set_time);
        showBackButton();
        bindView();
        bindAction();
    }

    private void bindView() {
        btnAddBreakfast = findViewById(R.id.btn_add_breakfast);
        btnAddLunch = findViewById(R.id.btn_add_lunch);
        btnAddDinner = findViewById(R.id.btn_add_dinner);
        btnAddLateDinner = findViewById(R.id.btn_add_late_dinner);
    }

    private void bindAction() {
        btnAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeFoodActivity.start(MealTime.Breakfast);
            }
        });
        btnAddLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeFoodActivity.start(MealTime.Lunch);
            }
        });
        btnAddDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeFoodActivity.start(MealTime.Dinner);
            }
        });
        btnAddLateDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeFoodActivity.start(MealTime.LateDinner);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
