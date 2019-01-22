package com.warunya.ricenearby.ui.settime;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.MealTime;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.FoodView;
import com.warunya.ricenearby.model.Food;
import com.warunya.ricenearby.ui.menu.MenuActivity;
import com.warunya.ricenearby.ui.settime.food.SetTimeFoodActivity;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetTimeActivity extends AbstractActivity implements SetTimeContract.View {

    private SetTimeContract.Presenter presenter = new SetTimePresenter(this);

    private CheckBox cbBreakfast;
    private CheckBox cbLunch;
    private CheckBox cbDinner;
    private CheckBox cbLateDinner;
    private Button btnAddBreakfast;
    private Button btnAddLunch;
    private Button btnAddDinner;

    private Button btnSelectDate;
    private Button btnAddMenu;
    private Button btnSave;
    private RecyclerView recyclerView;

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
//        cbBreakfast = findViewById(R.id.checkbox_breakfast);
//        cbLunch = findViewById(R.id.checkbox_lunch);
//        cbDinner = findViewById(R.id.checkbox_dinner);
//        cbLateDinner = findViewById(R.id.checkbox_late_dinner);
        btnAddBreakfast = findViewById(R.id.btn_add_breakfast);
        btnAddLunch = findViewById(R.id.btn_add_lunch);
        btnAddDinner = findViewById(R.id.btn_add_dinner);
//        btnSelectDate = findViewById(R.id.btn_select_date);
//        btnAddMenu = findViewById(R.id.btn_add_menu);
//        btnSave = findViewById(R.id.btn_save);
//        recyclerView = findViewById(R.id.recyclerView);

//        checkBoxList.add(cbBreakfast);
//        checkBoxList.add(cbLunch);
//        checkBoxList.add(cbDinner);
//        checkBoxList.add(cbLateDinner);

//        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
//            @Override
//            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
//                ((FoodView) itemView).bindAction();
//                ((FoodView) itemView).bind((Food) item);
//            }
//
//            @Override
//            public View onCreateView(ViewGroup parent) {
//                return new FoodView(getApplicationContext());
//            }
//        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

    }

    private void bindAction() {
//        setOnClickCheckbox(cbBreakfast);
//        setOnClickCheckbox(cbLunch);
//        setOnClickCheckbox(cbDinner);
//        setOnClickCheckbox(cbLateDinner);

//        btnSelectDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//            }
//        });
//
//        btnAddMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MenuActivity.startToSelectFood(SetTimeActivity.this);
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                presenter.submit(isSelectedDate, btnSelectDate.getText().toString(), checkBoxList, adapter.getList());
//            }
//        });

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
    }

//    private void showDatePickerDialog() {
//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//        };
//        new DatePickerDialog(SetTimeActivity.this, date, myCalendar
//                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//    }

//    private void updateLabel() {
//        String myFormat = "EE dd MMMM yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//
//        isSelectedDate = true;
//        btnSelectDate.setText(sdf.format(myCalendar.getTime()));
//    }

//    private void setOnClickCheckbox(final CheckBox clickedCheckBox) {
//        clickedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    for (CheckBox checkBox : checkBoxList) {
//                        checkBox.setChecked(clickedCheckBox == (checkBox));
//                    }
//                }
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 99) {
//                if (data == null) return;
//                List<Food> foodList = Parcels.unwrap(data.getParcelableExtra("foods"));
//                adapter.setListItem(foodList);
//            }
//        }
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void error(@NotNull String message) {

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
}
