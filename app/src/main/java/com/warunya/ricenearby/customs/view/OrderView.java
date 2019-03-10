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
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.customs.CustomAdapter;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.ui.confirmorder.ConfirmOrderActivity;
import com.warunya.ricenearby.utils.ConvertDateUtils;

public class OrderView extends LinearLayout {

    private Order order;
    private boolean isMyOrder = false;
    private CustomAdapter<Cart> adapter;

    private LinearLayout layoutItem;
    private TextView tvOrderNo;
    private TextView tvTotalPrice;
    private TextView tvDate;
    private TextView tvAmount;
    private TextView tvStatus;
    private RecyclerViewProgress recyclerViewProgress;

    public OrderView(Context context) {
        super(context);
        init();
    }

    public OrderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OrderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_order, this);
        getRootView().setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvOrderNo = findViewById(R.id.tv_order_no);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvDate = findViewById(R.id.tv_date);
        tvAmount = findViewById(R.id.tv_amount);
        tvStatus = findViewById(R.id.tv_status);
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress_food);
    }

    public void bind(final Order order, boolean isMyOrder) {
        if (order == null) return;
        this.order = order;
        this.isMyOrder = isMyOrder;

        int amount = 0;
        int totalPrice = 0;
        if (order.carts != null) {
            for (Cart cart : order.carts) {
                amount += cart.amount;
                if (order.totalPrice == 0) {
                    totalPrice += cart.food.price * cart.amount;
                }
            }
        } else if (order.cart != null) {
            amount += order.cart.amount;
            if (order.totalPrice == 0) {
                totalPrice += order.cart.food.price * order.cart.amount;
            }
        }

        String orderNo = order.orderNo == null ? order.key : order.orderNo;
        tvOrderNo.setText("Order no. " + orderNo);
        tvTotalPrice.setText(order.totalPrice == 0 ? totalPrice + AppInstance.DELIVERY_PRICE + ".-"
                : order.totalPrice + ".-");
        tvAmount.setText("จำนวน " + String.valueOf(amount) + " จาน");
        tvDate.setText("วันที่สั่งซื้อ: " + ConvertDateUtils.getDate(order.timestamp));
        tvStatus.setText(getResources().getString(order.orderStatus.getNameId()));

        tvStatus.setBackground(getResources().getDrawable(order.orderStatus.getBgId()));

        initRecyclerView();
        if (order.carts != null) {
            adapter.setListItem(order.carts);
        } else if (order.cart != null) {
            adapter.addItem(order.cart);
        }
    }

    private void initRecyclerView() {
        recyclerViewProgress.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter<>(new CustomAdapter.OnBindViewListener() {
            @Override
            public <T> void onBindViewHolder(T item, View itemView, int viewType, final int position) {
                final Cart cart = ((Cart) item);
                if (cart == null) return;
                ((CartView) itemView).bindAction();
                ((CartView) itemView).bind(cart, false);
            }

            @Override
            public View onCreateView(ViewGroup parent) {
                return new CartView(parent.getContext());
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

        tvStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderActivity.start(order, isMyOrder);
            }
        });
    }


}
