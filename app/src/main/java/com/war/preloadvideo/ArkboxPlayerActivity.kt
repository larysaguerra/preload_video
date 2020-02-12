package com.war.preloadvideo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_exo_player.*
import java.util.concurrent.TimeUnit

class ArkboxPlayerActivity : AppCompatActivity() {

    private var currentPosition: Int = 0
    private lateinit var stateAdapter: ArkboxPlayerFragmentStateAdapter
    private var countDownTimer: CountDownTimer? = null
    private val videoList = Utils.getVideoList()

    private var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        preparePager()
        rotateItem()
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun preparePager() {
        stateAdapter = ArkboxPlayerFragmentStateAdapter(this)
        stateAdapter.addVideoFragmentList(videoList.map { it.name })
        stateAdapter.mOnFinishVideo = object : OnFinishVideo {
            override fun onCompletionVideo() {
            }
        }

        pagerExoPlayer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (currentPosition != position) {
                    stateAdapter.getVideoFragment(currentPosition).pauseVideo(player)
                    currentPosition = position

                }
                rotateItem()
                stateAdapter.getVideoFragment(position).playVideo(player)
            }
        })
        pagerExoPlayer.offscreenPageLimit = 3
        pagerExoPlayer.currentItem = currentPosition
        pagerExoPlayer.adapter = stateAdapter
    }

    private fun rotateItem() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        countDownTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(10)) {
                override fun onFinish() {
                    cancel()
                    if (pagerExoPlayer.currentItem == stateAdapter.itemCount - 1) {
                        pagerExoPlayer.currentItem = 0
                    } else {
                        pagerExoPlayer.currentItem = pagerExoPlayer.currentItem + 1
                    }

                    player?.let {
                        if (it.hasNext()) {
                            it.next()
                        } else {
                            it.seekToDefaultPosition(0)
                        }
                    }
                }

                override fun onTick(p0: Long) {

                }
            }
        countDownTimer?.start()
    }

    private fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(this).build()
        }

        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(
            videoList.map {
                buildMediaSource(Uri.parse(it.path))
            }
        )

        player?.playWhenReady = true
        player?.prepare(concatenatingMediaSource, false, false)
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(this, "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

}

class ArkboxPlayerFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity), OnFinishVideo {

    var mOnFinishVideo: OnFinishVideo? = null

    private var fragmentList = arrayListOf<ArkboxPlayerFragment>()

    override fun getItemCount() =
        fragmentList.size

    override fun createFragment(position: Int) =
        fragmentList[position]

    fun addVideoFragmentList(newList: List<String>) {
        newList.forEach {
            val fragment = ArkboxPlayerFragment.newInstance(it)
            fragment.onFinishVideo = this
            fragmentList.add(fragment)
        }
    }

    fun getVideoFragment(position: Int) =
        fragmentList[position]

    override fun onCompletionVideo() {
        mOnFinishVideo?.onCompletionVideo()
    }

}

class VideoArkbox(val tag: Int, val name: String, val path: String)