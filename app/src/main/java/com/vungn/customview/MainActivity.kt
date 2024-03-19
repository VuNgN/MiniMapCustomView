package com.vungn.customview

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vungn.customview.topappbar.TopAppBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topAppBar = findViewById<TopAppBar>(R.id.topAppBar)
        topAppBar.imageBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.logo_thuy_loi)

    }
}