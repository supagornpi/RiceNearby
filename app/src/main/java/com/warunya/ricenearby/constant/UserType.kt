package com.warunya.ricenearby.constant

import java.util.*

enum class UserType {

    Normal,
    Seller,
    Sender,
    Admin;

    companion object {
        fun parse(type: Int): UserType {
            val creatorMap = HashMap<Int, UserType>()
            creatorMap[Normal.ordinal] = Normal
            creatorMap[Seller.ordinal] = Seller
            creatorMap[Sender.ordinal] = Sender
            creatorMap[Admin.ordinal] = Admin
            return if (creatorMap[type] == null) Normal else creatorMap[type]!!
        }
    }
}