package com.warunya.ricenearby.constant;

import com.warunya.ricenearby.R;

public enum OrderStatus {

    NotPaid(R.string.order_status_not_paid, R.drawable.background_color_red),
    WaitingForReview(R.string.order_status_waiting_for_review, R.drawable.background_color_yellow),
    Paid(R.string.order_status_paid, R.drawable.background_color_green),
    Sending(R.string.order_status_sending, 0),
    Received(R.string.order_status_received, 0);

    int nameId;
    int bgId;

    OrderStatus(int nameId, int bgId) {
        this.nameId = nameId;
        this.bgId = bgId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getBgId() {
        return bgId;
    }
}
