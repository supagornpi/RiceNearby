package com.warunya.ricenearby.ui.addfood;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;

public class AddFoodActivity extends AbstractActivity implements AddFoodContract.View {

    private EditText edtFoodName;
    private EditText edtAmount;
    private EditText edtPrice;
    private EditText edtDetail;
    private Button btnSave;

    private AddFoodContract.Presenter presenter = new AddFoodPresenter(this);

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, AddFoodActivity.class);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_add_food;
    }

    @Override
    protected void setupView() {
        setTitle(R.string.title_add_food);
        showBackButton();
        bindView();
        bindAction();
    }

    private void bindView() {
        edtFoodName = findViewById(R.id.edt_food_name);
        edtAmount = findViewById(R.id.edt_amount);
        edtPrice = findViewById(R.id.edt_price);
        edtDetail = findViewById(R.id.edt_detail);
        btnSave = findViewById(R.id.btn_save);

    }

    private void bindAction() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewFood(getFood());
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

    private Food getFood() {
        String uid = UserManager.getUid();
        String foodName = edtFoodName.getText().toString().trim();
        int amount = Integer.parseInt(edtAmount.getText().toString().trim());
        int price = Integer.parseInt(edtPrice.getText().toString().trim());
        String detail = edtDetail.getText().toString().trim();
        return new Food(uid, foodName, amount, price, detail);
    }
}
