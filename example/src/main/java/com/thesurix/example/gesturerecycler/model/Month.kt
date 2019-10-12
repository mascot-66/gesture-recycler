package com.thesurix.example.gesturerecycler.model


import androidx.annotation.DrawableRes

class Month(override val name: String, @param:DrawableRes val drawableId: Int) : MonthItem {

    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.MONTH
}
