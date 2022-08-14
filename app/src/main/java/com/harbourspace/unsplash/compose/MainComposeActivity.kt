package com.harbourspace.unsplash.compose

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.harbourspace.unsplash.R
import com.harbourspace.unsplash.UnsplashViewModel
import com.harbourspace.unsplash.data.AppDatabase
import com.harbourspace.unsplash.model.ImageUrl
import com.harbourspace.unsplash.model.UnsplashItem

class MainComposeActivity : AppCompatActivity() {

    private val unsplashViewModel: UnsplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        unsplashViewModel.fetchImages()

        setContent {

            val unsplashItems = unsplashViewModel.unsplashItems.observeAsState()

            unsplashViewModel.error.observe(this) {
                Toast.makeText(baseContext, R.string.main_unable_to_fetch_images, Toast.LENGTH_SHORT).show()
            }
            val image = unsplashItems.value?.get(0)

            MaterialTheme {

                if (image != null) {
                    NavigationButtons(image)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 100.dp)
                ) {
                    item() {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (image != null) {
                                AddUnsplashImage(image)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun NavigationButtons(image: UnsplashItem) {
        Row(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) Color.White else Color.Black),
                    onClick = {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                ) {
                    Text(
                        color = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) Color.Black else Color.White,
                        text = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) "Light Mode" else "Dark Mode"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Button(onClick = {
                    unsplashViewModel.fetchImages()
                }) {
                    Text(text = "New Quote")
                }
            }
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Button(onClick = {
                    saveImageUrlToDb(image)
                }) {
                    Text(text = "Save Quote")
                }
            }
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Button(onClick = {
                    openSaveImagesScreen()
                }) {
                    Text(text = "Saved images")
                }
            }
        }
    }

    private fun saveImageUrlToDb(image : UnsplashItem) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "image-url"
        )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
        val imageUrlDao = db.imageUrlDao()
        imageUrlDao.insertAll(ImageUrl(image.id, image.urls.regular, image.user.name!!, image.description))
    }

    @SuppressLint("StringFormatMatches")
    @Composable
    fun AddUnsplashImage(image: UnsplashItem) {
        val context = LocalContext.current
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.urls.regular)
                .transformations(listOf(CircleCropTransformation()))
                .build()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    Toast
                        .makeText(
                            context,
                            getString(R.string.main_item_clicked, image.id),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    openDetailsActivity(image)
                },
            backgroundColor = colorResource(id = R.color.purple_500)
        ) {

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.description_image_preview),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {

                Text(
                    text = image.description ?: "",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = image.user.name ?: "",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    private fun openSaveImagesScreen() {
        val intent = Intent(this, SavedQuotesActivity::class.java)
        startActivity(intent)
    }

    private fun openDetailsActivity(image: UnsplashItem) {
        val intent = Intent(this, DetailsComposeActivity::class.java)
        intent.putExtra("imageId", image.id)
        startActivity(intent)
    }
}