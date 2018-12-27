package com.warunya.ricenearby.ui.addfood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.warunya.ricenearby.model.FoodType;
import com.warunya.ricenearby.model.Upload;
import com.warunya.ricenearby.ui.addfood.adapter.AddImageAdapter;
import com.warunya.ricenearby.ui.addfood.adapter.FoodTypeAdapter;
import com.warunya.ricenearby.utils.FileUtils;
import com.warunya.ricenearby.utils.IntentUtils;
import com.warunya.ricenearby.utils.PermissionUtils;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddFoodActivity extends AbstractActivity implements AddFoodContract.View {

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final String EXTRA_EDIT_MODE = "EXTRA_EDIT_MODE";
    private static final String EXTRA_FOOD = "EXTRA_FOOD";

    private boolean isEditMode = false;
    private List<FoodType> foodTypeList = new ArrayList<>();
    private Food food;

    private RecyclerViewProgress recyclerViewProgress;
    private EditText edtFoodName;
    private EditText edtAmount;
    private EditText edtPrice;
    private EditText edtDetail;
    private Button btnSave;
    private RecyclerView recyclerViewType;

    private AddFoodContract.Presenter presenter = new AddFoodPresenter(this);
    private AddImageAdapter addImageAdapter;
    private FoodTypeAdapter typeAdapter;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, AddFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    public static void startInEditMode(Food food) {
        Intent intent = new Intent(MyApplication.applicationContext, AddFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_EDIT_MODE, true);
        intent.putExtra(EXTRA_FOOD, Parcels.wrap(food));
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_add_food;
    }

    @Override
    protected void setupView() {
        isEditMode = getIntent().getBooleanExtra(EXTRA_EDIT_MODE, false);
        if (isEditMode) {
            food = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_FOOD));
        }
        setTitle(R.string.title_add_food);
        showBackButton();
        bindView();
        bindAction();

        if (isEditMode) {
            if (food.uploads != null) {
                List<FoodImage> foodImages = new ArrayList<>();
                for (Upload upload : food.uploads) {
                    foodImages.add(new FoodImage(upload.author, upload.name, upload.url));
                }
                addImageAdapter.setImages(foodImages);
            }
            edtFoodName.setText(food.foodName);
            edtAmount.setText(food.amount + "");
            edtPrice.setText(food.price + ".-");
            edtDetail.setText(food.detail);
        }
    }

    private void bindView() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        recyclerViewType = findViewById(R.id.recyclerViewType);
        edtFoodName = findViewById(R.id.edt_food_name);
        edtAmount = findViewById(R.id.edt_amount);
        edtPrice = findViewById(R.id.edt_price);
        edtDetail = findViewById(R.id.edt_detail);
        btnSave = findViewById(R.id.btn_save);

        addImageAdapter = new AddImageAdapter(new AddImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked() {
                openGalleryIntent();
            }
        });

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.food_type_list));
        for (String name : list) {
            foodTypeList.add(new FoodType(name));
        }

        typeAdapter = new FoodTypeAdapter(foodTypeList);

        if (isEditMode && food.foodTypes != null) {
            for (FoodType foodType : foodTypeList) {
                for (FoodType foods : food.foodTypes) {
                    if (foodType.typeName.equals(foods.typeName)) {
                        foodType.isSelected = foods.isSelected;
                    }
                }
            }
            typeAdapter.setList(foodTypeList);
        }

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewProgress.recyclerView.setAdapter(addImageAdapter);

        recyclerViewType.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewType.setAdapter(typeAdapter);

        typeAdapter.setOnItemClickListener(new FoodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(final FoodTypeAdapter.ViewHolder viewHolder, final int position, final boolean isSelected) {

                viewHolder.checkBox.post(new Runnable() {
                    @Override
                    public void run() {
                        int selectedCount = 0;
                        for (FoodType foodType : foodTypeList) {
                            if (foodType.isSelected) selectedCount++;
                        }

                        if (selectedCount < 2) {
                            viewHolder.checkBox.setChecked(isSelected);
                        } else {
                            viewHolder.checkBox.setChecked(false);
                        }
                        foodTypeList.get(position).isSelected = isSelected;
                    }
                });
            }
        });
    }

    private void bindAction() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditMode) {
                    presenter.editFood(getFood());
                } else {
                    presenter.addNewFood(getFood());
                }
            }
        });
    }

    private Food getFood() {
        String uid = isEditMode ? food.key : UserManager.getUid();
        String foodName = edtFoodName.getText().toString().trim();
        int amount = 0;
        int price = 0;
        try {
            amount = Integer.parseInt(edtAmount.getText().toString().trim());
            price = Integer.parseInt(edtPrice.getText().toString().trim().replace(".-", ""));
        } catch (NumberFormatException e) {
            //do nothing
        }
        String detail = edtDetail.getText().toString().trim();

        List<FoodType> foodTypes = new ArrayList<>();
        for (FoodType foodType : foodTypeList) {
            if (foodType.isSelected) {
                foodTypes.add(foodType);
            }
        }

        return new Food(uid, foodName, amount, price, detail, foodTypes, addImageAdapter.getFoodImages());
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
