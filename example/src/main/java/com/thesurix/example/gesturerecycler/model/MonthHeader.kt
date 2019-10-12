package com.thesurix.example.gesturerecycler.model

class MonthHeader(override val name: String) : MonthItem {

    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.HEADER
}
