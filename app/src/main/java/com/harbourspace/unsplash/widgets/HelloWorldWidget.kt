package com.harbourspace.unsplash.widgets

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.text.Text
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.harbourspace.unsplash.R
import com.harbourspace.unsplash.UnsplashViewModel
import com.harbourspace.unsplash.data.UnsplashApiProvider
import com.harbourspace.unsplash.data.cb.UnsplashResult
import com.harbourspace.unsplash.model.UnsplashItem
import kotlinx.coroutines.launch
import java.net.URL

class HelloWorldWidget : GlanceAppWidget(), UnsplashResult {

    private lateinit var image : MutableState<UnsplashItem?>

    private lateinit var bitmap : MutableState<Bitmap?>

    @Composable
    override fun Content() {

        image = remember { mutableStateOf(null)}
        bitmap = remember { mutableStateOf(null)}

        UnsplashApiProvider().fetchImages(this)

        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = image.value) {
            if (image.value != null) {
                scope.launch {
                    val url = URL(image.value?.urls?.regular ?: "")
                    Log.d("HELLLO1", url.toString())
                    bitmap.value = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    Log.d("HELLLO2", bitmap.value.toString())
                }
            }
        }


        Text(text = "Quote of the day")
        bitmap.value?.let {
            Log.d("HELLLO3", it.asImageBitmap().toString())
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = stringResource(id = R.string.description_image_preview),
            )
        }
    }

    override fun onDataFetchedSuccess(images: List<UnsplashItem>) {
        Log.d("IMAGE URL", images[0].urls.regular)
        image.value = images[0]
    }

    override fun onPhotoByIdFetchedSuccess(image: UnsplashItem) {
        TODO("Not yet implemented")
    }

    override fun onDataFetchedFailed() {
        TODO("Not yet implemented")
    }
}