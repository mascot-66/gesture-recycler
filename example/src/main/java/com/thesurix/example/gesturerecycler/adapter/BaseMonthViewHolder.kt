package com.thesurix.example.gesturerecycler.adapter

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.thesurix.example.gesturerecycler.R
import com.thesurix.gesturerecycler.GestureViewHolder

abstract class  BaseMonthViewHolder(rootView: View) : GestureViewHolder(rootView) {
    abstract val monthText: TextView
    abstract val monthPicture: ImageView
    abstract val itemDrag: ImageView
    abstract val foreground: View?
    abstract val background: ViewStub?

    override val draggableView: View?
        get() = itemDrag

    override val foregroundView: View
        get() = foreground ?: super.foregroundView

    override val backgroundView: View?
        get() = background

    override fun onItemSelect() {
        val textColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white)
        val textColorTo = ContextCompat.getColor(itemView.context, R.color.indigo_500)
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getTextAnimatorListener(monthText, this))
            start()
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, R.color.indigo_500)
        val backgroundColorTo = ContextCompat.getColor(itemView.context, android.R.color.white)
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getBackgroundAnimatorListener(monthText, this))
            start()
        }
    }

    override fun onItemClear() {
        val textColorFrom = ContextCompat.getColor(itemView.context, R.color.indigo_500)
        val textColorTo = ContextCompat.getColor(itemView.context, android.R.color.white)
        ValueAnimator.ofObject(ArgbEvaluator(), textColorFrom, textColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getTextAnimatorListener(monthText, this))
            start()
        }


        val backgroundColorFrom = ContextCompat.getColor(itemView.context, android.R.color.white)
        val backgroundColorTo = ContextCompat.getColor(itemView.context, R.color.indigo_500)
        ValueAnimator.ofObject(ArgbEvaluator(), backgroundColorFrom, backgroundColorTo).apply {
            duration = itemView.context.resources.getInteger(R.integer.animation_speed_ms).toLong()
            addUpdateListener(getBackgroundAnimatorListener(monthText, this))
            start()
        }
    }

    override fun canDrag() = true

    override fun canSwipe() = true

    private fun getBackgroundAnimatorListener(view: TextView, animator: ValueAnimator): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener { view.setBackgroundColor(animator.animatedValue as Int) }
    }

    private fun getTextAnimatorListener(view: TextView, animator: ValueAnimator): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener { view.setTextColor(animator.animatedValue as Int) }
    }

}