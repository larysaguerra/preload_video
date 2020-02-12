package com.war.preloadvideo

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout


class CustomFragment : Fragment() {

    private var videoPosition: Int = -1
    private lateinit var videoPath: String
    private lateinit var mPlayerView: PlayerView

    private var player: SimpleExoPlayer? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    /**
     * El sistema lo llama cuando crea el fragmento. En tu implementación,
     * debes inicializar componentes esenciales del fragmento que quieras conservar
     * cuando el fragmento se pause o se detenga, y luego se reanude.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPosition = it.getInt(Utils.ARG_VIDEO_POSITION, -1)
            videoPath = it.getString(Utils.ARG_VIDEO_PATH) ?: ""
        }
    }

    /**
     * El sistema lo llama cuando el fragmento debe diseñar su interfaz de usuario por primera vez.
     * A fin de diseñar una IU para tu fragmento, debes mostrar un View desde este método,
     * que será la raíz del diseño de tu fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_custom, container, false)
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        val linearContainer = rootView.findViewById<LinearLayout>(R.id.linearContainer)
        mPlayerView = rootView.findViewById(R.id.mPlayerView)

        if (videoPosition.rem(2) == 0) {
            linearContainer.setBackgroundColor(Color.MAGENTA)
        } else {
            linearContainer.setBackgroundColor(Color.CYAN)
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        context?.let { c ->
            if (player == null) {
                player = SimpleExoPlayer.Builder(c).build()
            }
            player?.playWhenReady = true
            player?.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
//                    if (isPlaying) {
//                     slideView(mPlayerView, 0, 600)
//                    }
                }
            })
            player?.prepare(Utils.buildMediaSource(c, Uri.parse(videoPath)), false, false)
            mPlayerView.player = player
        }
    }

    private fun slideView(view: View, currentHeight: Int, newHeight: Int) {

        val slideAnimator = ValueAnimator
            .ofInt(currentHeight, newHeight)
            .setDuration(500)

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener { valueAnimator ->
            val value: Int = valueAnimator.animatedValue as Int
            view.layoutParams.height = value
            view.requestLayout()
        }

        /*  We use an animationSet to play the animation  */

        val animationSet = AnimatorSet()
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.play(slideAnimator)
        animationSet.start()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(position: Int, videoPath: String) =
            CustomFragment().apply {
                arguments = Bundle().apply {
                    putInt(Utils.ARG_VIDEO_POSITION, position)
                    putString(Utils.ARG_VIDEO_PATH, videoPath)
                }
            }

    }

}