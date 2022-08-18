package com.harbourspace.unsplash.compose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.harbourspace.unsplash.db.AppDatabase
import com.harbourspace.unsplash.model.ImageUrl

class SavedQuotesActivity : AppCompatActivity() {

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "image-url"
        )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
        val imageUrls = db.imageUrlDao().getAll()

        setContent {
            MaterialTheme {
                OutlinedTextFieldComposable()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 100.dp)
                ) {
                    items(imageUrls.count()) {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val context = LocalContext.current
                            val painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrls[it].url)
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
                                                    imageUrls[it]
                                                ),
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        openDetailsActivity(imageUrls[it])
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
                                        text = imageUrls[it].description ?: "",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = imageUrls[it].authorName ?: "",
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
    }

    @Composable
    fun OutlinedTextFieldComposable() {
        var text by remember { mutableStateOf("Search") }
        Row(
            modifier = Modifier.padding(start = 60.dp, top = 12.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Search") },
            )
        }
    }

    private fun openDetailsActivity(imageUrl: ImageUrl) {
        val intent = Intent(this, DetailsComposeActivity::class.java)
        intent.putExtra("imageId", imageUrl.id)
        startActivity(intent)
    }
}