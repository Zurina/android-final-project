package com.harbourspace.unsplash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import coil.load
import com.harbourspace.unsplash.model.UnsplashItem

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val detailIMageUrl = intent.extras!!.get("detailIMageUrl") as String
        val imageView = findViewById<View>(R.id.iv_preview) as ImageView
        imageView.load(detailIMageUrl)
    }
}