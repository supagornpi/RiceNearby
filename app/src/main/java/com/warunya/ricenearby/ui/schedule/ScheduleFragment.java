package com.warunya.ricenearby.ui.schedule;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.OrderByMealView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.MealByDate;
import com.warunya.ricenearby.model.OrderByDate;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends AbstractFragment implements ScheduleContract.View {

    private ScheduleContract.Presenter presenter;
    private CustomAdapter<OrderByDate> orderByDateAdapter;
    private CustomAdapter<MealByDate> adapter;
    private Filter currentFilter = Filter.OneWeek;
    private Calendar myCalendar = Calendar.getInstance();
    private String currentSelectedDate = "";

    private RecyclerViewProgress recyclerViewProgressSpecial;
    private TextView tvFilterName;
    private LinearLayout layoutFilter;
    private ImageView ivPrevious;
    private ImageView ivNext;

    public static void start() {
        Intent intent = new Intent(MyApplication.applicationContext, ScheduleFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void setupView(@NotNull View view) {
        setTitle("Schedule");
        bindView(view);
        bindAction();

        presenter = new SchedulePresenter(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getOrder(currentFilter);
    }

    private void bindView(View view) {
        recyclerViewProgressSpecial = view.findViewById(R.id.recyclerViewProgress_special_mode);
        tvFilterName = view.findViewById(R.id.tv_filter_name);
        layoutFilter = view.findViewById(R.id.layout_filter);
        ivPrevious = view.findViewById(R.id.iv_previous);
        ivNext = view.findViewById(R.id.iv_next);

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerViewProgressSpecial.setTextNotFound("ไม่มีอาหาร");
        recyclerViewProgressSpecial.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                final MealByDate mealByDate = ((MealByDate) item);
                if (mealByDate == null) return;
                ((OrderByMealView) itemView).bindAction();
                ((OrderByMealView) itemView).bind(mealByDate);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new OrderByMealView(parent.getContext());
            }
        });

        recyclerViewProgressSpecial.recyclerView.setAdapter(adapter);
    }


    private void bindAction() {
        tvFilterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar.add(Calendar.DAY_OF_MONTH, -1);
                updateCurrentSelected();
                presenter.filterByDate(myCalendar);

            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendar.add(Calendar.DAY_OF_MONTH, 1);
                updateCurrentSelected();
                presenter.filterByDate(myCalendar);

            }
        });
    }

    @Override
    public void showProgress() {
        recyclerViewProgressSpecial.showProgress();
    }

    @Override
    public void hideProgress() {
        recyclerViewProgressSpecial.hideProgress();
    }

    @Override
    public void showNotFound() {
        recyclerViewProgressSpecial.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgressSpecial.hideNotFound();
    }

    @Override
    public void fetchSummaryOrder(List<MealByDate> mealByDates) {
        adapter.setListItem(mealByDates);
    }

    private void showDatePickerDialog() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                presenter.filterByDate(myCalendar);
                updateCurrentSelected();

            }
        };
        new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateCurrentSelected() {
        String myFormat = "EE dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("th"));

        currentSelectedDate = sdf.format(myCalendar.getTime());
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        if (myCalendar.equals(current)) {
            tvFilterName.setText("วันนี้");
        } else {
            tvFilterName.setText(currentSelectedDate);
        }


    }
}
