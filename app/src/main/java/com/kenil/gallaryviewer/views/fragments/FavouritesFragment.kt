package com.kenil.gallaryviewer.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kenil.gallaryviewer.interfaces.Media
import com.kenil.gallaryviewer.adapters.FavouritesAdapter
import com.kenil.gallaryviewer.database.GallaryDatabase
import com.kenil.gallaryviewer.database.dao.FavouriteDao
import com.kenil.gallaryviewer.database.entity.FavouriteMedia
import com.kenil.gallaryviewer.databinding.FragmentFavouritesBinding
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.activities.ViewMediaActivity
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible

class FavouritesFragment() : Fragment(), Media {

    private var binding: FragmentFavouritesBinding? = null
    lateinit var favouriteDao: FavouriteDao
    var path: String = ""
    var mediaArrayList: List<FavouriteMedia> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()

        favouriteDao = GallaryDatabase.getDatabase(activity as Context).getFavouriteDao()

        mediaArrayList = favouriteDao.getFavouriteMedia()
        if (mediaArrayList.size == 0) {
            binding?.textViewNoDataFound?.doVisible()
            binding?.recyclerViewFavourites?.doHide()
        } else {
            binding?.textViewNoDataFound?.doHide()
            binding?.recyclerViewFavourites?.doVisible()
        }
        binding?.recyclerViewFavourites?.adapter =
            FavouritesAdapter(context as Activity, mediaArray = mediaArrayList, this)
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