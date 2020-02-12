package com.war.preloadvideo

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import java.io.File

class ItemFragment : Fragment(),
    TextureView.SurfaceTextureListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private lateinit var videoPath: String
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getString(Utils.ARG_VIDEO_PATH) ?: ""
            Log.d("guerra", "Video Path: " + videoPath)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_media_manager, container, false)
        val mTexture = rootView.findViewById<TextureView>(R.id.mTexture)
        mTexture.surfaceTextureListener = this
        return rootView
    }

    override fun onError(mPlayer: MediaPlayer?, p1: Int, p2: Int): Boolean {
        mediaPlayer = mPlayer
        return false
    }

    override fun onPrepared(mPlayer: MediaPlayer?) {
        mediaPlayer = mPlayer
        mediaPlayer?.start()
    }

    override fun onCompletion(mPlayer: MediaPlayer?) {
        mediaPlayer = mPlayer

    }

    override fun onStop() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onStop()
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
        return false
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        p0?.let {
            context?.let { c ->
                val file = File(videoPath)
                if (file.exists()) {
                    Log.e("guerra", "Preparar archivo "+file.absolutePath)
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setSurface(Surface(it))
                    mediaPlayer?.setOnErrorListener(this)
                    mediaPlayer?.setOnPreparedListener(this)
                    mediaPlayer?.setOnCompletionListener(this)
                    mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    mediaPlayer?.setDataSource(c, Uri.fromFile(file))
                    mediaPlayer?.prepareAsync()
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(videoPath: String) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putString(Utils.ARG_VIDEO_PATH, videoPath)
                }
            }
    }

}