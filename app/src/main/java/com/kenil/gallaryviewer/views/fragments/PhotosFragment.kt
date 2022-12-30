package com.kenil.gallaryviewer.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kenil.gallaryviewer.R
import com.kenil.gallaryviewer.adapters.PhotosAdapter
import com.kenil.gallaryviewer.databinding.FragmentPhotosBinding
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.activities.ViewMediaActivity
import com.kenil.gallaryviewer.views.utils.ShowToast
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible

class PhotosFragment() : Fragment(), com.kenil.gallaryviewer.interfaces.Media {
    private val REQUEST_PERMISSION = 100
    private var binding: FragmentPhotosBinding? = null
    var listOfAllImages: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerViewPhotos?.adapter =
            PhotosAdapter(context as Activity, listOfAllImages, this)
        if (listOfAllImages.size == 0) {
            binding?.textViewNoDataFound?.doVisible()
            binding?.recyclerViewPhotos?.doHide()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                (activity as Context), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (activity as Activity),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            );
        } else {
            queryImageStorage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                queryImageStorage()
                (context as Activity).ShowToast(getString(R.string.permission_granted))
            }
        }
    }

    private fun queryImageStorage() {
        listOfAllImages.clear()

        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = (activity as Context)?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.MediaColumns.DATA),
            null,
            null,
            imageSortOrder
        )

        cursor.use {
            it?.let {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (it.moveToNext()) {

                    var absolutePathOfImage = cursor?.getString(idColumn);
                    listOfAllImages.add(absolutePathOfImage!!);

                    if (listOfAllImages.size > 0) {
                        binding?.textViewNoDataFound?.doHide()
                        binding?.recyclerViewPhotos?.doVisible()
                    }
                }

                binding?.recyclerViewPhotos?.adapter =
                    PhotosAdapter(context as Activity, listOfAllImages, this)

            } ?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }
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