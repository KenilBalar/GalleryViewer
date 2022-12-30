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
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.utils.doHide
import kotlinx.android.synthetic.main.griditem_img.view.*

class PhotosAdapter(var context: Context, var imageArray: ArrayList<String>, var media: Media) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.griditem_img, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            imageViewPlay.doHide()
            imageViewGrid.doHide()

            Glide.with(context).load(imageArray.get(position))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground).into(imageView)

            cardView.setOnClickListener(View.OnClickListener {
                media.onMediaClick(imageArray[position], Const.IMAGE)
            })
        }
    }

    override fun getItemCount(): Int {
        return imageArray.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var cardView = view.cardView
        var imageView = view.imageView
        var imageViewPlay: ImageView = view.imageView_play
        var imageViewGrid: ImageView = view.imageView_grid
    }
}