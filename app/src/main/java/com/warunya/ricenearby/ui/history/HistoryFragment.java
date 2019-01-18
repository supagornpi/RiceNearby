package com.warunya.ricenearby.ui.history;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.base.AbstractFragment;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.customs.view.OrderView;
import com.warunya.ricenearby.customs.view.RecyclerViewProgress;
import com.warunya.ricenearby.model.Order;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryFragment extends AbstractFragment implements HistoryContract.View {

    private HistoryContract.Presenter presenter = new HistoryPresenter(this);
    private CustomAdapter<Order> orderAdapter;

    private RecyclerViewProgress recyclerViewProgress;

    @Override
    protected int setLayoutView() {
        return R.layout.fragment_history;
    }

    @Override
    protected void setupView(@NotNull View view) {
        setTitle("History");
        bindView(view);

    }

    private void bindView(View view) {
        recyclerViewProgress = view.findViewById(R.id.recyclerViewProgress);

        orderAdapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, int position) {
                final Order order = ((Order) item);
                if (order == null) return;
                ((OrderView) itemView).bindAction();
                ((OrderView) itemView).bind(order);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new OrderView(parent.getContext());
            }
        });

        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProgress.recyclerView.setAdapter(orderAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
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
