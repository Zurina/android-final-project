package com.harbourspace.unsplash.widgets

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.harbourspace.unsplash.data.UnsplashApiProvider
import com.harbourspace.unsplash.data.cb.UnsplashResult
import com.harbourspace.unsplash.model.UnsplashItem

private const val IMAGE_URL = "image.url"

class UnsplashAction : ActionCallback, UnsplashResult {

    private var imageUrl = ""

    override suspend fun onRun(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
            it.toMutablePreferences().apply {

                UnsplashApiProvider().fetchImages(this@UnsplashAction)

                var count = 0
                while (imageUrl == "" && count < 5) {
                    Thread.sleep(1000)
                    count++
                }

                if (imageUrl != "") {
                    this[stringPreferencesKey(IMAGE_URL)] = imageUrl
                } else {
                    this[stringPreferencesKey(IMAGE_URL)] = "https://img1.freepng.es/20171220/zoe/android-logo-png-5a3ab0bc11e079.0040072615137957720732.jpg"
                }
            }
        }

        HelloWorldWidget().update(context, glanceId)
    }

    override fun onDataFetchedSuccess(images: List<UnsplashItem>) {
        imageUrl = images[0].urls.regular
    }

    override fun onPhotoByIdFetchedSuccess(image: UnsplashItem) {
        TODO("Not yet implemented")
    }

    override fun onDataFetchedFailed() {
        TODO("Not yet implemented")
    }
}