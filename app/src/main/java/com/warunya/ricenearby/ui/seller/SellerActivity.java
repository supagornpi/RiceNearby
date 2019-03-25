package com.warunya.ricenearby.ui.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.address.AddressActivity;
import com.warunya.ricenearby.ui.history.HistoryActivity;
import com.warunya.ricenearby.ui.menu.MenuActivity;
import com.warunya.ricenearby.ui.settime.SetTimeActivity;

public class SellerActivity extends AbstractActivity implements SellerContract.View {

    private Button btnAddFood;
    private LinearLayout layoutMenu;
    private LinearLayout layoutSetTime;
    private LinearLayout layoutOrder;

    private SellerContract.Presenter presenter = new SellerPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, SellerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_seller;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle("ผู้ขาย");
        showBackButton();
//        setToolbarColor(android.R.color.transparent);
        bindView();
        bindAction();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    private void bindView() {
        btnAddFood = findViewById(R.id.btn_add_food);
        layoutMenu = findViewById(R.id.layout_menu);
        layoutSetTime = findViewById(R.id.layout_set_time);
        layoutOrder = findViewById(R.id.layout_order);
    }

    private void bindAction() {
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.hasAddress()) {
                    AddFoodActivity.start();
                } else {
                    DialogAlert.show(SellerActivity.this, "คุณต้องเพิ่มที่อยู่ก่อน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddressActivity.start();
                        }
                    });
                }
            }
        });
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.start();
            }
        });
        layoutSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeActivity.start();
            }
        });
        layoutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryActivity.start(true);
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNotFound() {

    }

    @Override
    public void hideNotFound() {

    }
}
