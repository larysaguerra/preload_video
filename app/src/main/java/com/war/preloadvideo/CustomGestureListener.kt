package com.war.preloadvideo

import android.view.GestureDetector
import android.view.MotionEvent
import java.util.*
import kotlin.math.abs

const val INITIAL_TIME_ELAPSED = 600
const val MIN_TIME_ELAPSED = 500
const val SWIPE_MIN_DISTANCE = 120
const val SWIPE_THRESHOLD_VELOCITY = 200

class CustomGestureListener(private val customEvents: CustomEvents) :
    GestureDetector.SimpleOnGestureListener() {


    private var lastTimeFling = 0L

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        try {

            val currentTime = Calendar.getInstance().timeInMillis

            if (lastTimeFling == 0L) {
                lastTimeFling = currentTime.minus(INITIAL_TIME_ELAPSED)
            }

            val diffTimeElapsed = currentTime - lastTimeFling

            if (diffTimeElapsed > MIN_TIME_ELAPSED) {

                lastTimeFling = currentTime

                if (e1 != null && e2 != null) {

                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x

                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            if (diffX > 0) {
                                customEvents.onSwipeForward()
                            } else {
                                customEvents.onSwipeBackward()
                            }
                            return true
                        }
                    } else if (abs(diffY) > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                        return false
                    }

                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

}

interface CustomEvents {

    fun onSwipeForward()

    fun onSwipeBackward()

}

