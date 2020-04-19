package com.androidtestapp.revolut.ui.util

import android.text.Editable
import android.text.TextWatcher
import java.util.*
import kotlin.concurrent.schedule

class OnTextChangeListener(val onTextChanged: (text: String) -> Unit) : TextWatcher {
    var timer = Timer()

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        timer.cancel()
        val sleep = when (s?.length) {
            1 -> 1000L
            2, 3 -> 700L
            4, 5 -> 500L
            else -> 200L
        }
        timer = Timer()
        timer.schedule(sleep) {
            onTextChanged(s.toString())
        }
    }
}