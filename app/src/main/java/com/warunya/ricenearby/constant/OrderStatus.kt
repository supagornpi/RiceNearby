package com.warunya.ricenearby.constant

import com.warunya.ricenearby.R
import org.parceler.Parcel
import java.util.*

@Parcel
enum class OrderStatus(val nameId: Int, val bgId: Int) {

    NotPaid(R.string.order_status_not_paid, R.drawable.background_color_red),
    WaitingForReview(R.string.order_status_waiting_for_review,  R.drawable.background_color_yellow),
    Paid(R.string.order_status_paid,  R.drawable.background_color_green),
    Sending(R.string.order_status_sending, 0),
    Received(R.string.order_status_received, 0);


    companion object {
        fun parse(type: Int): OrderStatus {
            val creatorMap = HashMap<Int, OrderStatus>()
            creatorMap[NotPaid.ordinal] = NotPaid
            creatorMap[Paid.ordinal] = Paid
            creatorMap[Sending.ordinal] = Sending
            creatorMap[Received.ordinal] = Received
            return if (creatorMap[type] == null) NotPaid else creatorMap[type]!!
        }
    }
}