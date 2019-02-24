package com.warunya.ricenearby.ui.menu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.dialog.DialogAlert;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.addfood.AddFoodActivity;
import com.warunya.ricenearby.ui.address.AddressActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AbstractActivity implements MenuContract.View {

    public static final String EXTRA_IS_SELECT_FOOD = "EXTRA_IS_SELECT_FOOD";
    private MenuContract.Presenter presenter = new MenuPresenter(this);
    private CustomAdapter<Food> adapter;
    private boolean isSelectFood = false;

    private RecyclerViewProgress viewProgress;
    private ProgressBar progressBar;
    private TextView tvNotFound;
    private CheckBox cbSelectedAll;
    private LinearLayout llSelectedAll;
    private Button btnSelect;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    public static void startToSelectFood(Activity activity) {
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.putExtra(EXTRA_IS_SELECT_FOOD, true);
        activity.startActivityForResult(intent, 99);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_menu;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle(R.string.title_menu);
        showBackButton();
        isSelectFood = getIntent().getBooleanExtra(EXTRA_IS_SELECT_FOOD, false);
        bindView();
        bindAction();

        presenter.start();
    }

    private void bindView() {
        viewProgress = findViewById(R.id.recyclerViewProgress);
        progressBar = findViewById(R.id.progressBar);
        cbSelectedAll = findViewById(R.id.checkbox_selected_all);
        llSelectedAll = findViewById(R.id.layout_selected_all);
        btnSelect = findViewById(R.id.btn_select);

        if (isSelectFood) {
            btnSelect.setVisibility(View.VISIBLE);
            llSelectedAll.setVisibility(View.VISIBLE);
        }

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodView) itemView).bindAction();
                ((FoodView) itemView).bind((Food) item);
                if (isSelectFood) {
                    ((FoodView) itemView).showCheckBox();
                }
                ((FoodView) itemView).setSelected(((Food) item).isSelected);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodView(getApplicationContext());
            }
        });
        viewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewProgress.recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        if (!isSelectFood) {
            setMenuRightText("เพิ่มอาหาร");
            setOnclickMenuRight(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (presenter.hasAddress()) {
                        AddFoodActivity.start();
                    } else {
                        DialogAlert.Companion.show(MenuActivity.this, "คุณต้องเพิ่มที่อยู่ก่อน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AddressActivity.start();
                            }
                        });
                    }
                }
            });
        }

        if (isSelectFood) {
            cbSelectedAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    List<Food> foodList = adapter.getList();
                    for (Food food : foodList) {
                        food.isSelected = b;
                    }
                    adapter.setListItem(foodList);
                }
            });
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Food> foodList = new ArrayList<>();
                    for (Food food : adapter.getList()) {
                        if (food.isSelected) {
                            foodList.add(food);
                        }
                    }
                    if (foodList.size() != 0) {
                        Intent data = new Intent();
                        data.putExtra("foods", Parcels.wrap(foodList));
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        Toast.makeText(MenuActivity.this, "กรุณาเลือกอย่างน้อย 1 อย่าง", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void bindItem(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void showProgress() {
        viewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        viewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        viewProgress.showProgress();
    }

    @Override
    public void hideNotFound() {
        viewProgress.hideProgress();
    }
}
