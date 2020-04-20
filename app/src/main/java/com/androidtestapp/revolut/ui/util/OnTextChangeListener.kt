package com.androidtestapp.revolut.ui.util

import android.text.Editable
import android.text.TextWatcher
import com.androidtestapp.revolut.AppConstants.EDIT_TEXT_CHANGE_LISTENER_DELAY
import java.util.*
import kotlin.concurrent.schedule

class OnTextChangeListener(val onTextChanged: (text: String) -> Unit) : TextWatcher {
    var timer = Timer()

    override fun afterTextChanged(s: Editable?) {
        // No Implementation Required
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // No Implementation Required
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        timer.cancel()
        timer = Timer()
        timer.schedule(EDIT_TEXT_CHANGE_LISTENER_DELAY) {
            onTextChanged(s.toString())
        }
        onTextChanged(s.toString())
    }
}