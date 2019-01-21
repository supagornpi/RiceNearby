package com.warunya.ricenearby.ui.settime.food;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodMealView;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.model.Meal;
import com.warunya.ricenearby.ui.menu.MenuActivity;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetTimeFoodActivity extends AbstractActivity implements SetTimeFoodContract.View {

    private SetTimeFoodContract.Presenter presenter = new SetTimeFoodPresenter(this);
    private Calendar myCalendar = Calendar.getInstance();
    private CustomAdapter<Food> adapter;
    private MealTime mealTime;
    private String currentSelectedDate = "";

    private Button btnAddMenu;
    private RecyclerView recyclerView;

    public static void start(MealTime mealTime) {
        Intent intent = new Intent(MyApplication.applicationContext, SetTimeFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MealTime", mealTime.ordinal());
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_set_time_food;
    }

    @Override
    protected void setupView(Bundle savedInstanceState) {
        setTitle(R.string.title_set_time);
        showBackButton();

        mealTime = MealTime.Companion.parse(getIntent().getIntExtra("MealTime", 0));
        bindView();
        bindAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.findFoodWithMealTime(mealTime);

    }

    private void bindView() {
        btnAddMenu = findViewById(R.id.btn_add_menu);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                ((FoodMealView) itemView).bindAction(new FoodMealView.OnButtonClickListener() {
                    @Override
                    public void onClickedAddDate(Food food) {
                        showDatePickerDialog(food);
                    }

                    @Override
                    public void onClickedDelete(String key) {
                        presenter.removeAllMeals(key);
                    }

                    @Override
                    public void onClickedDeleteChild(String key, String childKey) {
                        presenter.removeMeal(key, childKey);

                    }
                });
                ((FoodMealView) itemView).bind((Food) item, mealTime);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new FoodMealView(parent.getContext());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(null);
            }
        });
    }

    private void showDatePickerDialog(final Food food) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                if (food == null) {
                    MenuActivity.startToSelectFood(SetTimeFoodActivity.this);
                } else {
                    addMealTime(food, mealTime, currentSelectedDate);
                }
            }
        };
        new DatePickerDialog(SetTimeFoodActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "EE dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        currentSelectedDate = sdf.format(myCalendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 99) {
                if (data == null) return;
                List<Food> foodList = Parcels.unwrap(data.getParcelableExtra("foods"));

                for (Food food : foodList) {
                    addMealTime(food, mealTime, currentSelectedDate);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public void fetchFoods(List<Food> foods) {
        adapter.setListItem(foods);
    }

    @Override
    public void pleaseSelectedDate() {
        Toast.makeText(this, "กรุณาเลือกวันที่", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void pleaseSelectedMeal() {
        Toast.makeText(this, "กรุณาเลือกมื้ออาหาร", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pleaseSelectedMenu() {
        Toast.makeText(this, "กรุณาเลือกเมนูอย่างน้อย 1 อย่าง", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitSuccess() {
        finish();
    }

    private void addMealTime(Food food, MealTime mealTime, String date) {
        boolean isInAdapter = false;
        int index = 0;
        for (Food foodInAdapter : adapter.getList()) {
            if (foodInAdapter.key.equals(food.key)) {
                isInAdapter = true;
                food = foodInAdapter;
            }
            index++;
        }

        Meal meal = new Meal(date);
        if (mealTime == MealTime.Breakfast) {
            //new array if it null
            if (food.breakfasts == null) {
                food.breakfasts = new ArrayList<>();
            }
            food.breakfasts.add(new Meal(date));
        } else if (mealTime == MealTime.Lunch) {
            //new array if it null
            if (food.lunches == null) {
                food.lunches = new ArrayList<>();
            }
            food.lunches.add(new Meal(date));
        } else if (mealTime == MealTime.Dinner) {
            //new array if it null
            if (food.dinners == null) {
                food.dinners = new ArrayList<>();
            }
            food.dinners.add(new Meal(date));
        }

        if (isInAdapter) {
            adapter.editItemAt(index - 1, food);
            adapter.notifyDataSetChanged();
        } else {
            adapter.addItem(food);
        }
        presenter.addMeal(food.key, meal, mealTime);
    }
}
