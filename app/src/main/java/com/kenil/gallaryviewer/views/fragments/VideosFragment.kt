package com.kenil.gallaryviewer.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kenil.gallaryviewer.interfaces.Media
import com.kenil.gallaryviewer.adapters.VideosAdapter
import com.kenil.gallaryviewer.databinding.FragmentVideosBinding
import com.kenil.gallaryviewer.models.VideoModel
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.activities.ViewMediaActivity
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible


class VideosFragment() : Fragment(), Media {
    private var binding: FragmentVideosBinding? = null
    var videoList: ArrayList<VideoModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideosBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerViewVideos?.adapter = VideosAdapter(context as Activity, videoList, this)

        if (videoList.size == 0) {
            binding?.textViewNoDataFound?.doVisible()
            binding?.recyclerViewVideos?.doHide()
        }
    }

    override fun onResume() {
        super.onResume()

        fn_video()
    }

    fun fn_video() {
        videoList.clear()

        var uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Thumbnails.DATA
        )
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        var cursor = (activity as Context).getContentResolver().query(
            uri, projection, null, null, "$orderBy DESC"
        )!!
        val column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)
        while (cursor.moveToNext()) {
            val absolutePathOfImage = cursor.getString(column_index_data)

            val videoModel = VideoModel()
            videoModel.isSelected = false
            videoModel.path = absolutePathOfImage
            videoModel.thumb = cursor.getString(thum)
            videoList.add(videoModel)
        }

        if (videoList.size > 0) {
            binding?.textViewNoDataFound?.doHide()
            binding?.recyclerViewVideos?.doVisible()
        }

        binding?.recyclerViewVideos?.adapter = VideosAdapter(activity as Context, videoList, this)
    }

    override fun onMediaClick(path: String, type: String, isFromFavourite: Boolean) {
        Intent(activity as Context, ViewMediaActivity::class.java).apply {
            putExtra(Const.PATH, path)
            putExtra(Const.TYPE, type)
            putExtra(Const.IS_FROM, isFromFavourite)
            startActivity(this)
        }
    }
}