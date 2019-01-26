package com.warunya.ricenearby.customs.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warunya.ricenearby.R;
import com.warunya.ricenearby.constant.AppInstance;
import com.warunya.ricenearby.model.Cart;
import com.warunya.ricenearby.model.Order;
import com.warunya.ricenearby.ui.confirmorder.ConfirmOrderActivity;

import java.util.Calendar;
import java.util.Locale;

public class OrderView extends LinearLayout {

    private Order order;

    private LinearLayout layoutItem;
    private TextView tvOrderNo;
    private TextView tvTotalPrice;
    private TextView tvDate;
    private TextView tvAmount;
    private TextView tvStatus;

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

        tvOrderNo = findViewById(R.id.tv_order_no);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvDate = findViewById(R.id.tv_date);
        tvAmount = findViewById(R.id.tv_amount);
        tvStatus = findViewById(R.id.tv_status);
    }

    public void bind(final Order order) {
        if (order == null) return;
        this.order = order;

        int amount = 0;
        int totalPrice = 0;
        for (Cart cart : order.carts) {
            amount += cart.amount;
            if (order.totalPrice == 0) {
                totalPrice += cart.food.price * cart.amount;
            }
        }

        tvOrderNo.setText("Order no. " + order.key);
        tvTotalPrice.setText(order.totalPrice == 0 ? totalPrice + AppInstance.DELIVERY_PRICE + ".-"
                : order.totalPrice + ".-");
        tvAmount.setText("จำนวน " + String.valueOf(amount) + " จาน");
        tvDate.setText("วันที่สั่งซื้อ: " + getDate(order.timestamp));
        tvStatus.setText(getResources().getString(order.orderStatus.getNameId()));

        tvStatus.setBackground(getResources().getDrawable(order.orderStatus.getBgId()));
    }

    public void bindAction() {
        layoutItem = findViewById(R.id.layout_item);

        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmOrderActivity.start(order.key);
            }
        });
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format(AppInstance.DATE_FORMAT_DEFAULT, cal).toString();
    }
}
