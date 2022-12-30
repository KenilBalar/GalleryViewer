package com.kenil.gallaryviewer.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.kenil.gallaryviewer.adapters.ViewPagerAdapter
import com.kenil.gallaryviewer.databinding.ActivityHomeBinding
import com.kenil.gallaryviewer.views.fragments.FavouritesFragment
import com.kenil.gallaryviewer.views.fragments.PhotosFragment
import com.kenil.gallaryviewer.views.fragments.VideosFragment

class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupViewPager(binding?.tabViewpager!!)
        binding?.tabTablayout?.setupWithViewPager(binding?.tabViewpager)
    }

    private fun setupViewPager(viewpager: ViewPager) {
        var adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PhotosFragment(), "Photos")
        adapter.addFragment(VideosFragment(), "Videos")
        adapter.addFragment(FavouritesFragment(), "Favourites")
        viewpager.setAdapter(adapter)
    }
}