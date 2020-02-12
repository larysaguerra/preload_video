package com.war.preloadvideo

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import java.io.File


class ExoPlayerFragment : Fragment(), OnVideoActions {

    private lateinit var videoPath: String
    private lateinit var mPlayerView: PlayerView
    private lateinit var mPlayerView2: PlayerView
    private var exoPlayer: SimpleExoPlayer? = null
    private var exoPlayer2: SimpleExoPlayer? = null
    var onFinishVideo: OnFinishVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getString(Utils.ARG_VIDEO_PATH) ?: ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        exoPlayer = null
        exoPlayer2?.release()
        exoPlayer2 = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_exo_player, container, false)
        mPlayerView = rootView.findViewById(R.id.mPlayerView)
        mPlayerView2 = rootView.findViewById(R.id.mPlayerView2)
        exoPlayer = SimpleExoPlayer.Builder(mPlayerView.context).build()
        exoPlayer2 = SimpleExoPlayer.Builder(mPlayerView.context).build()

        mPlayerView.player = exoPlayer
        mPlayerView2.player = exoPlayer2

        exoPlayer?.playWhenReady = false
        exoPlayer?.prepare(buildMediaSource(Uri.fromFile(File(videoPath))), false, false)

        exoPlayer2?.playWhenReady = false
        exoPlayer2?.prepare(
            buildMediaSource(Uri.fromFile(File("storage/emulated/0/Arkbox/MediaList/72a4c31f-a7d6-411d-b055-242b6d6b259c.mp4"))),
            false,
            false
        )
        return rootView
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    override fun playVideo() {
        exoPlayer?.playWhenReady = true
        exoPlayer2?.playWhenReady = true
    }

    override fun pauseVideo() {
        exoPlayer?.playWhenReady = false
        exoPlayer?.seekTo(0, 0)
        exoPlayer2?.playWhenReady = false
        exoPlayer2?.seekTo(0, 0)
    }

    override fun stopVideo() {
        exoPlayer?.release()
        exoPlayer2?.release()
    }

    companion object {

        @JvmStatic
        fun newInstance(videoPath: String) =
            ExoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(Utils.ARG_VIDEO_PATH, videoPath)
                }
            }
    }

}