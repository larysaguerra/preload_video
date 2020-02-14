package com.war.preloadvideo

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File

class Utils {

    companion object {

        const val ARG_VIDEO_POSITION = "arg_video_position"
        const val ARG_VIDEO_PATH = "arg_video_path"

        fun buildMediaSource(context: Context, uri: Uri): MediaSource {
            val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-codelab")
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }

        fun getVideoList(): List<VideoArkbox> {
            val directory = File("storage/emulated/0/Arkbox/MediaList")
            //get file list in directory
            return directory.listFiles()
                .filterNotNull()
                //exclude names from used file list
                .filter { it.extension.endsWith("mp4") }
                .mapIndexed { index, file ->
                    Log.d("guerra", "$index) Video ${file.name}")
                    VideoArkbox(index, file.name, file.path)
                }
        }

    }

}