package com.harbourspace.unsplash.compose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.harbourspace.unsplash.DetailsActivity
import com.harbourspace.unsplash.R
import com.harbourspace.unsplash.data.AppDatabase
import com.harbourspace.unsplash.model.ImageUrl

class SavedQuotesActivity : AppCompatActivity() {

    @SuppressLint("StringFormatMatches")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "image-url"
        ).allowMainThreadQueries().build()
        val imageUrls = db.imageUrlDao().getAll()

        setContent {
            MaterialTheme {
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
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        Toast
                                            .makeText(
                                                context,
                                                getString(R.string.main_item_clicked, imageUrls[it]),
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
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }


    private fun openDetailsActivity(imageUrl: ImageUrl) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("detailIMageUrl", imageUrl.url)
        startActivity(intent)
    }
}