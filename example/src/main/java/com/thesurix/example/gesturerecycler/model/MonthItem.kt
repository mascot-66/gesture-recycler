package com.thesurix.example.gesturerecycler.model

interface MonthItem {

    val type: MonthItemType

    val name: String

    enum class MonthItemType {
        HEADER, MONTH
    }
}
