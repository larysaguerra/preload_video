package com.war.preloadvideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.activity_media_manager.*
import java.io.File

class MediaPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_manager)
        preparePager()
    }

    private fun preparePager() {
        val mediaManagerFragmentStateAdapter = MediaManagerFragmentStateAdapter(this)
        mediaManagerFragmentStateAdapter.addVideoList(getVideoList())
        pagerMediaManager.adapter = mediaManagerFragmentStateAdapter
    }

    private fun getVideoList(): List<String> {
        val directory = File("storage/emulated/0/Arkbox/MediaList")
        //get file list in directory
        return directory.listFiles()
            .filterNotNull()
            //exclude names from used file list
            .filter { it.extension.endsWith("mp4") }
            //get file path
            .map { it.path }
    }

}

class MediaManagerFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var videoPathList = listOf<String>()

    override fun getItemCount() =
        videoPathList.size

    override fun createFragment(position: Int) =
        ItemFragment.newInstance(videoPathList[position])

    fun addVideoList(newList: List<String>) {
        videoPathList = newList
    }

}