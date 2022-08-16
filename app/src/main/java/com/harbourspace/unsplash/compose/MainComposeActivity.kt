package com.harbourspace.unsplash.compose

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.harbourspace.unsplash.R
import com.harbourspace.unsplash.UnsplashViewModel
import com.harbourspace.unsplash.data.AppDatabase
import com.harbourspace.unsplash.model.ImageUrl
import com.harbourspace.unsplash.model.UnsplashItem

private enum class Tab(@StringRes val tab: Int) {
    QUOTES(R.string.main_tab_quotes),
    FAVORITES(R.string.main_tab_favorites)
}

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
                    AddTopTabNavigation(
                        image = image,
                    )
                }
            }
        }
    }

    @Composable
    fun OnTabSelected(
        selected: MutableState<Int>,
        image : UnsplashItem,
    ) {
        when(selected.value) {
            Tab.QUOTES.ordinal -> {
                //ChangeThemeButton()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 50.dp)
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

            Tab.FAVORITES.ordinal -> {
                savedImages()
            }
        }
    }

    @Composable
    fun AddTopTabNavigation(
        image: UnsplashItem,
    ) {
        val selected = remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.background(Color.LightGray)
        ) {

            val actions = listOf(Tab.QUOTES, Tab.FAVORITES)
            TabRow(
                selectedTabIndex = selected.value,
                modifier = Modifier.height(48.dp),
                indicator = @Composable { tabPositions: List<TabPosition> ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selected.value]),
                        color = Color.White
                    )
                },
                backgroundColor = Color.Black

            ) {
                actions.forEachIndexed { index, title ->

                    Tab(
                        selected = selected.value == index,
                        onClick = {
                            selected.value = index
                        }
                    ) {
                        Text(
                            text = stringResource(id = Tab.values()[index].tab),
                            color = Color.White
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent,
                content = {
                    OnTabSelected(
                        selected = selected,
                        image = image,
                    )
                }
            )
        }
    }

    @Composable
    fun ChangeThemeButton() {
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
                    openDetailsActivity(image.id)
                },
            backgroundColor = colorResource(id = R.color.purple_500)
        ) {

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.description_image_preview),
            )

            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = {
                        unsplashViewModel.fetchImages()
                    }) {
                    Text(
                        modifier = Modifier.background(Color.Black).padding(10.dp),
                        text = "New"
                    )
                }
                Button(
                    onClick = {
                        saveImageUrlToDb(image)
                    }) {
                    Text(
                        modifier = Modifier.background(Color.Black).padding(10.dp),
                        text = "Save"
                    )
                }
            }

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

    private fun openDetailsActivity(id: String) {
        val intent = Intent(this, DetailsComposeActivity::class.java)
        intent.putExtra("imageId", id)
        startActivity(intent)
    }

    private val _imageUrls = MutableLiveData<List<ImageUrl>>()
    private val imageUrlsLive: LiveData<List<ImageUrl>> = _imageUrls

    @SuppressLint("StringFormatMatches")
    @Composable
    private fun savedImages() {

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "image-url"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        _imageUrls.value = db.imageUrlDao().getAll()
        var imageUrls = imageUrlsLive.observeAsState()

        var text by remember { mutableStateOf("") }

        var searchFlag by remember { mutableStateOf(false) }
        if (searchFlag) {
            _imageUrls.value = db.imageUrlDao().getPhotosByAuthorName("%$text%")
            imageUrls = imageUrlsLive.observeAsState()
        }
        Row(
            modifier = Modifier.padding(start = 60.dp, top = 12.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    searchFlag = true
                },
                label = { Text("Search") },
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 100.dp)
        ) {
            items(imageUrls.value?.count() ?: 0) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val context = LocalContext.current
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrls.value?.get(it)?.url)
                            .transformations(listOf(CircleCropTransformation()))
                            .build()
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                Toast
                                    .makeText(
                                        context,
                                        getString(
                                            R.string.main_item_clicked,
                                            imageUrls.value?.get(it)
                                        ),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                imageUrls.value?.get(it)?.let { it1 -> openDetailsActivity(it1.id) }
                            },
                        backgroundColor = colorResource(id = R.color.purple_500)
                    ) {

                        Image(
                            painter = painter,
                            contentDescription = stringResource(id = R.string.description_image_preview),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            Text(
                                text = imageUrls.value?.get(it)?.description ?: "",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = imageUrls.value?.get(it)?.authorName ?: "",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}