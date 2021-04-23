package com.ryanconway.spaceflightnews.util

import android.os.CountDownTimer

class Debouncer(
    private val debounceTimeMillis: Long = 300L
) {

    private var timer: CountDownTimer? = null

    fun run(callback: () -> Unit) {
        timer?.cancel()
        timer = DebounceTimer(debounceTimeMillis, callback)
        timer?.start()
    }

    fun cancel() = timer?.cancel()

    private class DebounceTimer(
        debounceTimeMillis: Long,
        private val callback: () -> Unit
    ) : CountDownTimer(debounceTimeMillis, debounceTimeMillis) {
        override fun onTick(millisUntilFinished: Long) = Unit
        override fun onFinish() = callback()
    }
}