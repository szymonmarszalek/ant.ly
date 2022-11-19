package com.example.antly.helpers

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

object SoftKeyboard {
    fun hide(activity: Activity) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (activity.window != null) {
                activity.window.decorView
                val imm = activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
            }
        }, 300)
    }

    fun show(activity: Activity, targetInput: View) {
        targetInput.postDelayed({
            try {
                targetInput.requestFocus()
                val inputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(targetInput, InputMethodManager.SHOW_IMPLICIT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 500)
    }
}