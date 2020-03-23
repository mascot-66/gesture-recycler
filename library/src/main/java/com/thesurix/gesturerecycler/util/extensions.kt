package com.thesurix.gesturerecycler.util

/**
 * Swaps two items inside [MutableList]
 * @param firstIndex first index to swap
 * @param secondIndex second index to swap
 */
fun <T> MutableList<T>.swap(firstIndex: Int, secondIndex: Int) {
    val tmp = this[firstIndex]
    this[firstIndex] = this[secondIndex]
    this[secondIndex] = tmp
}

/**
 * Returns data offset based on header state.
 * @param headerEnabled header flag
 * @return data offset
 */
fun getDataOffset(headerEnabled: Boolean) = if (headerEnabled) -1 else 0