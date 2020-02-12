package com.war.preloadvideo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import java.util.concurrent.TimeUnit

class CustomActivity : AppCompatActivity(), CustomEvents {

    private var countDownTimer: CountDownTimer? = null
    private var currentPosition = 0
    private val itemList = Utils.getVideoList()
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        gestureDetector = GestureDetector(this, CustomGestureListener(this))

        restartRotation()
        addFragment()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun addFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val currentFragment = CustomFragment.newInstance(0, itemList[0].path)
        fragmentTransaction.add(R.id.fragmentCustom, currentFragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(position: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val currentFragment = CustomFragment.newInstance(position, itemList[position].path)
        fragmentTransaction.setCustomAnimations(
            android.R.animator.fade_in,
            android.R.animator.fade_out
        )
        fragmentTransaction.replace(R.id.fragmentCustom, currentFragment)
        fragmentTransaction.commit()
    }

    private fun getBeforePosition(): Int {
        return when (currentPosition) {
            0 -> itemList.size - 1
            else -> currentPosition - 1
        }
    }

    private fun getNextPosition(): Int {
        return when (currentPosition) {
            itemList.size - 1 -> 0
            else -> currentPosition + 1
        }
    }

    private fun restartRotation() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        countDownTimer =
            object : CountDownTimer(TimeUnit.SECONDS.toMillis(15), TimeUnit.SECONDS.toMillis(15)) {
                override fun onFinish() {
                    onSwipeForward()
                }

                override fun onTick(p0: Long) {

                }
            }
        countDownTimer?.start()
    }

    override fun onSwipeForward() {
        //todo pasar al siguente fragmento
        currentPosition = getNextPosition()
        replaceFragment(currentPosition)
        restartRotation()
    }

    override fun onSwipeBackward() {
        //todo pasar al fragmento anterior
        currentPosition = getBeforePosition()
        replaceFragment(currentPosition)
        restartRotation()
    }

}
