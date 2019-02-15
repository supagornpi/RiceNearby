package com.warunya.ricenearby.ui.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.constant.Filter;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.OrderView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Order;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryFragment extends AbstractFragment implements HistoryContract.View {

    private HistoryContract.Presenter presenter;
    private CustomAdapter<Order> orderAdapter;
    private Filter currentFilter = Filter.OneWeek;

    private RecyclerViewProgress recyclerViewProgress;
    private RelativeLayout actionBar;
    private TextView tvFilterName;
    private LinearLayout layoutFilter;

    public static HistoryFragment newInstance(boolean isMyOrder) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isMyOrder", isMyOrder);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_history;
    }

    @Override
    protected void setupView(@NotNull View view) {
        setTitle("History");
        bindView(view);
        bindAction();

        boolean isMyOrder = false;
        if (getArguments() != null) {
            isMyOrder = getArguments().getBoolean("isMyOrder", false);
        }
//        actionBar.setVisibility(isMyOrder ? View.GONE : View.VISIBLE);
        actionBar.setVisibility(View.GONE);
        presenter = new HistoryPresenter(this, isMyOrder);

    }

    private void bindView(View view) {
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);
        actionBar = view.findViewById(R.id.actionbar);
        tvFilterName = view.findViewById(R.id.tv_filter_name);
        layoutFilter = view.findViewById(R.id.layout_filter);

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

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProgress.recyclerView.setAdapter(orderAdapter);
    }

    private void bindAction() {
        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
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
    public void onResume() {
        super.onResume();
        presenter.filterOrder(currentFilter);
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
        orderAdapter.setListItem(orders);
    }
}
