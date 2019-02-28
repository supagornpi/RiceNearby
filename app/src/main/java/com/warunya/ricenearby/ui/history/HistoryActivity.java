package com.warunya.ricenearby.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractActivity;
import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.OrderByDateView;
import com.warunya.ricenearby.customs.view.OrderView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.model.OrderByDate;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HistoryActivity extends AbstractActivity implements HistoryContract.View {

    private HistoryContract.Presenter presenter;
    private CustomAdapter<Order> orderAdapter;
    private CustomAdapter<OrderByDate> orderByDateAdapter;
    private Filter currentFilter = Filter.OneWeek;
    private boolean normalMode = true;
    private boolean isMyOrder = false;

    private RecyclerViewProgress recyclerViewProgress;
    private RecyclerViewProgress recyclerViewProgressSpecial;
    private TextView tvFilterName;
    private LinearLayout layoutFilter;

    public static void start(boolean isMyOrder) {
        Intent intent = new Intent(MyApplication.applicationContext, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isMyOrder", isMyOrder);
        MyApplication.applicationContext.startActivity(intent);
    }

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_history;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
        bindView();
        bindAction();
        showBackButton();

        isMyOrder = getIntent().getBooleanExtra("isMyOrder", false);
        setTitle(isMyOrder ? "Order" : "History");

//        actionBar.setVisibility(isMyOrder ? View.GONE : View.VISIBLE);
        presenter = new HistoryPresenter(this, isMyOrder);

        if (isMyOrder) {
            setMenuRightText("สรุปรายวัน");
            setOnclickMenuRight(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.switchMode();
                    if (normalMode) {
                        normalMode = false;
                        setMenuRightText("ดูตามออเดอร์");
                    } else {
                        normalMode = true;
                        setMenuRightText("สรุปรายวัน");
                    }
                }
            });
        }
//        if (isMyOrder) {
//            presenter.filterOrder(currentFilter);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!isMyOrder) {
            presenter.filterOrder(currentFilter);
//        }
    }

    private void bindView() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress);
        recyclerViewProgressSpecial = findViewById(R.id.recyclerViewProgress_special_mode);
        tvFilterName = findViewById(R.id.tv_filter_name);
        layoutFilter = findViewById(R.id.layout_filter);

        initRecyclerViewNormailMode();
        initRecyclerViewSpecialMode();

    }

    private void initRecyclerViewNormailMode() {
        orderAdapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                final Order order = ((Order) item);
                if (order == null) return;
                ((OrderView) itemView).bindAction();
                ((OrderView) itemView).bind(order, presenter.isMyOrder());
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new OrderView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProgress.recyclerView.setAdapter(orderAdapter);
    }

    private void initRecyclerViewSpecialMode() {
        orderByDateAdapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                final OrderByDate orderByDate = ((OrderByDate) item);
                if (orderByDate == null) return;
                ((OrderByDateView) itemView).bindAction();
                ((OrderByDateView) itemView).bind(orderByDate);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new OrderByDateView(parent.getContext());
            }
        });

        recyclerViewProgressSpecial.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProgressSpecial.recyclerView.setAdapter(orderByDateAdapter);
    }

    private void bindAction() {
        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(HistoryActivity.this);
                builder.title("เลือกช่วงเวลา");
                builder.items(R.array.filter_list).itemsCallbackSingleChoice(currentFilter.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        tvFilterName.setText(text);
                        return false;
                    }
                });

                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        currentFilter = Filter.Companion.parse(dialog.getSelectedIndex());
                        presenter.filterOrder(currentFilter);
                    }
                }).negativeText("ยกเลิก").positiveText("ตกลง");
                builder.show();
            }
        });
    }

    @Override
    public void showProgress() {
        recyclerViewProgress.showProgress();
    }

    @Override
    public void hideProgress() {
        recyclerViewProgress.hideProgress();
    }

    @Override
    public void showNotFound() {
        recyclerViewProgress.showNotFound();
    }

    @Override
    public void hideNotFound() {
        recyclerViewProgress.hideNotFound();
    }

    @Override
    public void fetchOrder(List<Order> orders) {
        recyclerViewProgress.setVisibility(View.VISIBLE);
        recyclerViewProgressSpecial.setVisibility(View.GONE);
        orderAdapter.setListItem(orders);
    }

    @Override
    public void fetchSummaryOrder(List<OrderByDate> orderByDates) {
        recyclerViewProgress.setVisibility(View.GONE);
        recyclerViewProgressSpecial.setVisibility(View.VISIBLE);
        orderByDateAdapter.setListItem(orderByDates);

    }
}
