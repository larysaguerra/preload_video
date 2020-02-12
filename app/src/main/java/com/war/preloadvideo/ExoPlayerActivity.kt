package com.war.preloadvideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_exo_player.*
import java.util.concurrent.TimeUnit

class ExoPlayerActivity : AppCompatActivity() {

    private var currentPosition: Int = 0
    private lateinit var stateAdapter: ExoPlayerFragmentStateAdapter
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        preparePager()
        rotateItem()
    }

    private fun preparePager() {
        stateAdapter = ExoPlayerFragmentStateAdapter(this)
        stateAdapter.addVideoFragmentList(Utils.getVideoList().map { it.path })
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
                    stateAdapter.getVideoFragment(currentPosition).pauseVideo()
                    currentPosition = position
                }
                rotateItem()
                stateAdapter.getVideoFragment(position).playVideo()
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
                }

                override fun onTick(p0: Long) {

                }
            }
        countDownTimer?.start()
    }

}

class ExoPlayerFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity), OnFinishVideo {

    var mOnFinishVideo: OnFinishVideo? = null

    private var fragmentList = arrayListOf<ExoPlayerFragment>()

    override fun getItemCount() =
        fragmentList.size

    override fun createFragment(position: Int) =
        fragmentList[position]

    fun addVideoFragmentList(newList: List<String>) {
        newList.forEach {
            val fragment = ExoPlayerFragment.newInstance(it)
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