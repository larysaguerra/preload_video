package com.war.preloadvideo

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class ArkboxPlayerFragment : Fragment(), OnArkboxVideoActions {

    private lateinit var videoName: String
    private lateinit var textName: TextView
    private lateinit var mPlayerView: PlayerView

    var onFinishVideo: OnFinishVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoName = it.getString(Utils.ARG_VIDEO_PATH, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_arkbox_player, container, false)
        textName = rootView.findViewById(R.id.textName)
        mPlayerView = rootView.findViewById(R.id.mPlayerView)

        textName.text = videoName

        return rootView
    }

    override fun playVideo(exoPlayer: SimpleExoPlayer?) {
        mPlayerView.player = exoPlayer
//        exoPlayer?.playWhenReady = true
    }

    override fun pauseVideo(exoPlayer: SimpleExoPlayer?) {
        mPlayerView.player = null
//        exoPlayer?.playWhenReady = false
    }

    companion object {
        @JvmStatic
        fun newInstance(videoPath: String) =
            ArkboxPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(Utils.ARG_VIDEO_PATH, videoPath)
                }
            }
    }

}

interface OnArkboxVideoActions {

    fun playVideo(exoPlayer: SimpleExoPlayer?)

    fun pauseVideo(exoPlayer: SimpleExoPlayer?)

}