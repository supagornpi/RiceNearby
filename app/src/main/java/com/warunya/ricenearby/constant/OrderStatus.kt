package com.warunya.ricenearby.constant

import java.util.*

enum class OrderStatus {

    NotPaid,
    Paid,
    NotReceived,
    Received;

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