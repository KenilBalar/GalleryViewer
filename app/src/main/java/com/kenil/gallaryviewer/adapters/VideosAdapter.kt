package com.kenil.gallaryviewer.adapters


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kenil.gallaryviewer.R
import com.kenil.gallaryviewer.interfaces.Media
import com.kenil.gallaryviewer.models.VideoModel
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible
import kotlinx.android.synthetic.main.griditem_img.view.*


class VideosAdapter(var context: Context, var videoList: ArrayList<VideoModel>, var media: Media) :
    RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.griditem_img, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            Glide.with(context).load("file://" + videoList.get(position).thumb)
                .skipMemoryCache(false).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        imageViewPlay.doVisible()
                        imageViewGrid.doHide()

                        return false
                    }

                }).into(imageView)

            cardView.setOnClickListener(View.OnClickListener {
                media.onMediaClick(
                    videoList[position].path!!, Const.VIDEO
                )
            })
        }

    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imageView: ImageView = view.imageView
        var cardView: CardView = view.cardView
        var imageViewPlay: ImageView = view.imageView_play
        var imageViewGrid: ImageView = view.imageView_grid
    }
}