package com.war.preloadvideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_video_view.*
import java.util.concurrent.TimeUnit

class VideoViewActivity : AppCompatActivity() {

    private var currentPosition: Int = 0
    private var countDownTimer: CountDownTimer? = null
    private lateinit var videoFragmentAdapter: VideoViewFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        preparePager()
        rotateItem()
    }

    private fun preparePager() {
        videoFragmentAdapter = VideoViewFragmentStateAdapter(this)
        videoFragmentAdapter.mOnFinishVideo = object : OnFinishVideo {
            override fun onCompletionVideo() {
                if (pagerVideoView.currentItem == videoFragmentAdapter.itemCount - 1) {
                    pagerVideoView.currentItem = 0
                } else {
                    pagerVideoView.currentItem = pagerVideoView.currentItem + 1
                }
            }
        }
        videoFragmentAdapter.addVideoFragmentList(Utils.getVideoList().map { it.path })
        pagerVideoView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
//                    videoFragmentAdapter.getVideoFragment(currentPosition).pauseVideo()
                    currentPosition = position
                }
                rotateItem()
//                videoFragmentAdapter.getVideoFragment(position).playVideo()
            }
        })
        pagerVideoView.offscreenPageLimit = 2
        pagerVideoView.currentItem = currentPosition
        pagerVideoView.adapter = videoFragmentAdapter
    }

    private fun rotateItem() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        countDownTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(10)) {
                override fun onFinish() {
                    if (pagerVideoView.currentItem == videoFragmentAdapter.itemCount - 1) {
                        pagerVideoView.currentItem = 0
                    } else {
                        pagerVideoView.currentItem = pagerVideoView.currentItem + 1
                    }
                }

                override fun onTick(p0: Long) {

                }
            }
        countDownTimer?.start()
    }
}

class VideoViewFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity), OnFinishVideo {

    var mOnFinishVideo: OnFinishVideo? = null

    private var fragmentList = arrayListOf<VideoViewFragment>()

    override fun getItemCount() =
        fragmentList.size

    override fun createFragment(position: Int) =
        fragmentList[position]

    fun addVideoFragmentList(newList: List<String>) {
        var position = 0
        newList.forEach {
            val videoFragment = VideoViewFragment.newInstance(position, it)
            position++
            videoFragment.onFinishVideo = this
            fragmentList.add(videoFragment)
        }
        newList.reversed().forEach {
            val videoFragment = VideoViewFragment.newInstance(position, it)
            position++
            videoFragment.onFinishVideo = this
            fragmentList.add(videoFragment)
        }
    }

    fun getVideoFragment(position: Int) =
        fragmentList[position]

    override fun onCompletionVideo() {
        mOnFinishVideo?.onCompletionVideo()
    }

}