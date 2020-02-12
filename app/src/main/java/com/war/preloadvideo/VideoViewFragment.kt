package com.war.preloadvideo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment

private const val ARG_POSITION = "arg_position"
class VideoViewFragment : Fragment(), OnVideoActions {

    private var position: Int = -1
    private lateinit var videoPath: String
    private lateinit var mVideoView: VideoView
    private lateinit var textPosition: TextView
    var onFinishVideo: OnFinishVideo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION, -1)
            videoPath = it.getString(Utils.ARG_VIDEO_PATH, "")
//            Log.d("guerra", "$position Path: " + videoPath)
            Log.d("guerra", "$position onCreate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_video_view, container, false)

        textPosition = rootView.findViewById(R.id.textPosition)
        textPosition.text = "($position)"

        mVideoView = rootView.findViewById(R.id.mVideoView)
        mVideoView.setVideoPath(videoPath)
        mVideoView.setOnCompletionListener {
            onFinishVideo?.onCompletionVideo()
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("guerra", "$position onAttach")
    }

    override fun onPause() {
        super.onPause()
        Log.d("guerra", "$position onPause")
        pauseVideo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("guerra", "$position onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("guerra", "$position onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("guerra", "$position onResume")
        playVideo()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("guerra", "$position onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("guerra", "$position onDestroyView")
    }

    override fun onStop() {
        super.onStop()
        Log.d("guerra", "$position onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("guerra", "$position onDestroy")
    }

    override fun playVideo() {
        Log.d("guerra", "$position playVideo")
        mVideoView.start()
    }

    override fun pauseVideo() {
        Log.d("guerra", "$position pauseVideo")
        mVideoView.pause()
        mVideoView.seekTo(0)
    }

    override fun stopVideo() {
//        mVideoView.stopPlayback()
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, videoPath: String) =
            VideoViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putString(Utils.ARG_VIDEO_PATH, videoPath)
                }
            }
    }

}
