package com.harbourspace.unsplash.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.harbourspace.unsplash.R
import com.harbourspace.unsplash.UnsplashViewModel
import com.harbourspace.unsplash.model.UnsplashItem

class DetailsComposeActivity : AppCompatActivity() {

    private val unsplashViewModel: UnsplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageId = intent.extras!!.get("imageId") as String
        unsplashViewModel.fetchImageById(imageId)

        setContent {

            val unsplashItem = unsplashViewModel.unsplashItem.observeAsState()

            unsplashViewModel.error.observe(this) {
                Toast.makeText(baseContext, R.string.main_unable_to_fetch_images, Toast.LENGTH_SHORT).show()
            }

            val image = unsplashItem.value

            Surface(modifier = Modifier.fillMaxSize(), color = colors.background) {
                if (image != null) {
                    Greeting("Android", image)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, image : UnsplashItem) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image.urls.regular)
            .transformations(listOf(CircleCropTransformation()))
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.description_image_preview),
                modifier = Modifier.height(280.dp).fillMaxWidth(),
            )
            Box(
                modifier = Modifier.height(280.dp).fillMaxWidth(),
                contentAlignment = Alignment.BottomStart
            ) {
                image.description?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 19.sp,
                        modifier = Modifier.padding(10.dp).padding(start = 24.dp),
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_android),
                    contentDescription = "person",
                    modifier = Modifier.size(75.dp).padding(5.dp).clip(CircleShape),
                )

                Text(
                    text = image.user.name!!,
                    color = Color.White,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                color = Color.Green,
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                ) {

                    Text(
                        text = "Camera",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.exif?.model!!,
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Focal Length",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.exif?.focal_length!!,
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ISO",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.exif?.iso.toString(),
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                ) {

                    Text(
                        text = "Aperture",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.exif?.aperture!!,
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Shutter Speed",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.exif?.exposure_time!!,
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Dimensions",
                        color = Color.White,
                        fontSize = 19.sp,
                    )
                    Text(
                        text = image.width.toString() + " x " + image.height.toString(),
                        color = Color.Gray,
                        fontSize = 19.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                color = Color.Green,
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Views",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = "Downloads",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = "Likes",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = (0..100).random().toString() + "K",
                    color = Color.Gray,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = (0..100).random().toString() + "K",
                    color = Color.Gray,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
                Text(
                    text = image.likes.toString(),
                    color = Color.Gray,
                    fontSize = 19.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                color = Color.Green,
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Card(
                    elevation = 4.dp,
                    backgroundColor = Color.Gray,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Barcelona", fontWeight = FontWeight.W700, color = Color.White)
                    }
                }
                Card(
                    elevation = 4.dp,
                    backgroundColor = Color.Gray,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Spain", fontWeight = FontWeight.W700, color = Color.White)
                    }
                }
            }
        }
    }
}