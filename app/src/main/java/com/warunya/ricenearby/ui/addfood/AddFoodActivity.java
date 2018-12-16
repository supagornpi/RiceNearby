package com.warunya.ricenearby.ui.addfood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.firebase.UserManager;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.FoodImage;
import com.warunya.ricenearby.ui.addfood.adapter.AddImageAdapter;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class AddFoodActivity extends AbstractActivity implements AddFoodContract.View {

    private static final int REQUEST_IMAGE_GALLERY = 1;

    private RecyclerViewProgress recyclerViewProgress;
    private EditText edtFoodName;
    private EditText edtAmount;
    private EditText edtPrice;
    private EditText edtDetail;
    private Button btnSave;

    private AddFoodContract.Presenter presenter = new AddFoodPresenter(this);
    private AddImageAdapter addImageAdapter;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, AddFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        edtFoodName = findViewById(R.id.edt_food_name);
        edtAmount = findViewById(R.id.edt_amount);
        edtPrice = findViewById(R.id.edt_price);
        edtDetail = findViewById(R.id.edt_detail);
        btnSave = findViewById(R.id.btn_save);

        addImageAdapter = new AddImageAdapter(new ArrayList<FoodImage>(), new AddImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked() {
                openGalleryIntent();
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProgress.recyclerView.setAdapter(addImageAdapter);
    }

    private void bindAction() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewFood(getFood());
            }
        });
    }

    private Food getFood() {
        String uid = UserManager.getUid();
        String foodName = edtFoodName.getText().toString().trim();
        int amount = 0;
        int price = 0;
        try {
            amount = Integer.parseInt(edtAmount.getText().toString().trim());
            price = Integer.parseInt(edtPrice.getText().toString().trim());
        } catch (NumberFormatException e) {
            //do nothing
        }
        String detail = edtDetail.getText().toString().trim();
        return new Food(uid, foodName, amount, price, detail, addImageAdapter.getFoodImages());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                File file = null;
                if (data.getData() != null) {
                    file = FileUtils.getResizedBitmap(this, new File(FileUtils.getRealPathFromURI(this, data.getData())));
                }
                addImageAdapter.addImageUri(Uri.fromFile(file));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.Companion.getPERMISSION_READ_EXTERNAL_STORAGE()) {
            if (PermissionUtils.Companion.isGrantAll(permissions)) {
                openGalleryIntent();
            }
        }
    }

    private void openGalleryIntent() {
        IntentUtils.INSTANCE.startIntentGallery(this, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public void addSuccess() {
        finish();
    }
}
