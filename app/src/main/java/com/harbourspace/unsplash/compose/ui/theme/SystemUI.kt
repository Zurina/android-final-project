package com.harbourspace.unsplash.compose.ui.theme

import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class SystemUI(private val window: Window) {

    fun setStatusBarColor(statusBarColor: Color, darkIcons: Boolean = true) {
        window.statusBarColor = statusBarColor.toArgb()
        if (darkIcons) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

}