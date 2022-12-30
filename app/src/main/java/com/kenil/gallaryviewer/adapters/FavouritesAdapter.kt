package com.kenil.gallaryviewer.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kenil.gallaryviewer.R
import com.kenil.gallaryviewer.interfaces.Media
import com.kenil.gallaryviewer.database.entity.FavouriteMedia
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible
import kotlinx.android.synthetic.main.griditem_img.view.*

class FavouritesAdapter(
    var context: Context, var mediaArray: List<FavouriteMedia>, var media: Media
) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.griditem_img, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            when (mediaArray[position].mediaType) {
                Const.IMAGE -> {
                    imageViewPlay.doHide()
                    imageViewGrid.doHide()

                    Glide.with(context).load(mediaArray[position].path!!)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground).into(imageView)
                    cardView.setOnClickListener(View.OnClickListener {
                        media.onMediaClick(
                            mediaArray[position].path!!, Const.IMAGE, isFromFavourite = true
                        )
                    })
                }

                Const.VIDEO -> {
                    imageViewPlay.doVisible()
                    imageViewGrid.doHide()

                    Glide.with(context).load("file://" + mediaArray[position].path)
                        .skipMemoryCache(false).into(imageView)
                    cardView.setOnClickListener(View.OnClickListener {
                        media.onMediaClick(
                            mediaArray[position].path!!, Const.VIDEO, isFromFavourite = true
                        )
                    })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaArray.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView = view.cardView
        var imageView = view.imageView
        var imageViewPlay: ImageView = view.imageView_play
        var imageViewGrid: ImageView = view.imageView_grid
    }
}