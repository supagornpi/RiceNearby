package com.warunya.ricenearby.constant

import com.warunya.ricenearby.R
import java.util.*

enum class OrderStatus(val nameId: Int, val bgId: Int) {

    NotPaid(R.string.order_status_not_paid, R.drawable.background_color_yellow),
    Paid(R.string.order_status_Paid,  R.drawable.background_color_green),
    NotReceived(R.string.order_status_not_received, 0),
    Received(R.string.order_status_received, 0);


    companion object {
        fun parse(type: Int): OrderStatus {
            val creatorMap = HashMap<Int, OrderStatus>()
            creatorMap[NotPaid.ordinal] = NotPaid
            creatorMap[Paid.ordinal] = Paid
            creatorMap[NotReceived.ordinal] = NotReceived
            creatorMap[Received.ordinal] = Received
            return if (creatorMap[type] == null) NotPaid else creatorMap[type]!!
        }
    }
}