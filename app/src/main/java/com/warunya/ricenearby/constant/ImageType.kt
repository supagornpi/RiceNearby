package com.warunya.ricenearby.constant

import java.util.*

enum class ImageType {

    CopyIdCard,
    CopyBookBank;

    companion object {
        fun parse(type: Int): ImageType {
            val creatorMap = HashMap<Int, ImageType>()
            creatorMap[CopyIdCard.ordinal] = CopyIdCard
            creatorMap[CopyBookBank.ordinal] = CopyBookBank
            return if (creatorMap[type] == null) CopyIdCard else creatorMap[type]!!
        }
    }
}