package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.model.MealByDate;
import com.warunya.ricenearby.model.OrderByDate;
import com.warunya.ricenearby.utils.ConvertDateUtils;

public class OrderByDateView extends LinearLayout {

    private OrderByDate orderByDate;
    private boolean isMyOrder = false;
    private CustomAdapter<MealByDate> adapter;

    private LinearLayout layoutItem;
    private TextView tvDate;
    private RecyclerViewProgress recyclerViewProgress;

    public OrderByDateView(Context context) {
        super(context);
        init();
    }

    public OrderByDateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderByDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OrderByDateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_order_by_date, this);
        getRootView().setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvDate = findViewById(R.id.tv_date);
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress_meal_time);
    }

    public void bind(final OrderByDate orderByDate) {
        if (orderByDate == null) return;
        this.orderByDate = orderByDate;

        tvDate.setText(orderByDate.date);

        initRecyclerView();
        if (orderByDate.mealByDateList != null) {
            adapter.setListItem(orderByDate.mealByDateList);
        }
    }

    private void initRecyclerView() {
        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        recyclerViewProgress.recyclerView.setAdapter(adapter);
    }

    public void bindAction() {
        layoutItem = findViewById(R.id.layout_item);

        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewProgress.setVisibility(recyclerViewProgress.isShown() ? GONE : VISIBLE);
            }
        });

    }


}
