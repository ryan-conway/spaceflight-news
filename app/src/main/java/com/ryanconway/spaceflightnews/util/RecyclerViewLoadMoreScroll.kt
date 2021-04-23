package com.ryanconway.spaceflightnews.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll(
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int = 5,
    private val loadMoreCallback: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var isLoading: Boolean = false

    fun setLoaded() {
        isLoading = false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            loadMoreCallback()
            isLoading = true
        }

    }
}