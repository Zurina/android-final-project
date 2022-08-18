package com.harbourspace.unsplash.widgets

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import com.harbourspace.unsplash.data.UnsplashApiProvider
import com.harbourspace.unsplash.data.cb.UnsplashResult
import com.harbourspace.unsplash.model.UnsplashItem
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class HelloWorldWidget : GlanceAppWidget(), UnsplashResult {

    private lateinit var image : MutableState<UnsplashItem?>
    private lateinit var bitmap : MutableState<Bitmap?>

    private val IMAGE_URL = "image.url"

    @Composable
    override fun Content() {
        val pref = currentState<Preferences>()
        val imageUrl = pref[stringPreferencesKey(IMAGE_URL)] ?: ""

        if (imageUrl.isEmpty()) {
            Row(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .clickable(onClick = actionRunCallback<UnsplashAction>()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Click to load image...",
                    modifier = GlanceModifier
                        .defaultWeight()
                        .background(Color.Red)
                )
            }
        } else {
            val bitmap = getBitmapFromURL(imageUrl)
            bitmap?.let {
                val bm: ImageBitmap = it.asImageBitmap()
                Image(
                    provider = ImageProvider(bm.asAndroidBitmap()),
                    contentDescription = "Quote of the day",
                )
            }
        }
    }


    fun getBitmapFromURL(src: String?): Bitmap? {
            val url = URL(src)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
    }

    override fun onDataFetchedSuccess(images: List<UnsplashItem>) {
        Log.d("IMAGE URL", images[0].urls.regular)
        image.value = images[0]
    }

    override fun onPhotoByIdFetchedSuccess(image: UnsplashItem) {
        TODO("Not yet implemented")
    }

    override fun onDataFetchedFailed() {
        Log.d("IMAGE URL ", "failed on data fetch")
        image.value = null
    }
}