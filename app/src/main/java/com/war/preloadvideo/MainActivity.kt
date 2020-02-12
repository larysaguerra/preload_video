package com.war.preloadvideo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    fun onClickMediaManager(view: View){
        startActivity(Intent(this, MediaPlayerActivity::class.java))
    }

    fun onClickVideoView(view: View){
        startActivity(Intent(this, VideoViewActivity::class.java))
    }

    fun onClickExoPlayer(view: View){
        startActivity(Intent(this, ExoPlayerActivity::class.java))
    }

    fun onClickArkboxPlayer(view: View){
        startActivity(Intent(this, ArkboxPlayerActivity::class.java))
    }

    fun onClickCustom(view: View){
        startActivity(Intent(this, CustomActivity::class.java))
    }

}