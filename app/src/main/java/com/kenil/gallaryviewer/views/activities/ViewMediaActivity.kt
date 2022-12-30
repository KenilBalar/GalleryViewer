package com.kenil.gallaryviewer.views.activities

import android.app.PendingIntent
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kenil.gallaryviewer.R
import com.kenil.gallaryviewer.database.GallaryDatabase
import com.kenil.gallaryviewer.database.dao.FavouriteDao
import com.kenil.gallaryviewer.database.entity.FavouriteMedia
import com.kenil.gallaryviewer.databinding.ActivityViewMediaBinding
import com.kenil.gallaryviewer.utils.Const
import com.kenil.gallaryviewer.views.utils.ShowToast
import com.kenil.gallaryviewer.views.utils.doHide
import com.kenil.gallaryviewer.views.utils.doVisible
import java.io.File

class ViewMediaActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>
    private var binding: ActivityViewMediaBinding? = null
    lateinit var favouriteDao: FavouriteDao
    var path: String = ""
    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMediaBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        favouriteDao = GallaryDatabase.getDatabase(this).getFavouriteDao()

        launcher = registerForActivityResult<IntentSenderRequest, ActivityResult>(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show()
            }
        }

        with(binding!!) {
            if (intent.hasExtra(Const.PATH)) {
                path = intent.getStringExtra(Const.PATH)!!

                when (intent.getStringExtra(Const.TYPE)) {
                    Const.IMAGE -> {
                        cardView.doVisible()
                        videoView.doHide()
                        type = Const.IMAGE
                        Glide.with(this@ViewMediaActivity).load(path)
                            .placeholder(R.drawable.ic_launcher_foreground).fitCenter()
                            .into(imageView);
                    }
                    Const.VIDEO -> {
                        cardView.doHide()
                        videoView.doVisible()
                        type = Const.VIDEO
                        videoView.setVideoPath(path)
                        videoView.start()
                    }
                }
            }
            toolBar.setNavigationOnClickListener {
                finish()
            }

            btnFavourite.setOnClickListener(this@ViewMediaActivity)
            btnDelete.setOnClickListener(this@ViewMediaActivity)

            if (intent.getBooleanExtra(Const.IS_FROM, false)) {
                btnFavourite.doHide()
                btnDelete.setText("Remove From Favourite")
            }
        }
    }

    override fun onClick(v: View?) {

        when (v) {
            binding?.btnFavourite -> {

                if (!favouriteDao.isMediaExist(path)) {
                    favouriteDao.addData(FavouriteMedia(id = null, path = path, mediaType = type))
                    if (type == Const.IMAGE) this.ShowToast(getString(R.string.image_added_as_favourite))
                    else this.ShowToast(getString(R.string.video_added_as_favourite))

                    finish()
                } else {
                    this.ShowToast(getString(R.string.already_added_to_favourite))
                }

            }
            binding?.btnDelete -> {
                if (intent.getBooleanExtra(Const.IS_FROM, false)) {
                    favouriteDao.removeFromFavourite(path)
                    finish()
                    if (type == Const.IMAGE) this.ShowToast(getString(R.string.image_removed_from_favourite))
                    else this.ShowToast(getString(R.string.video_removed_from_favourite))
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        var mediaID = getFilePathToMediaID(File(path).absolutePath, this)
                        val Uri_one: Uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.getContentUri("external"), mediaID
                        )
                        delete(launcher, Uri_one)
                    } else {
                        val fdelete = File(path)
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                this.ShowToast(getString(R.string.deleted_successfully))
                            } else {
                                this.ShowToast(getString(R.string.error_occurred_while_deleting))
                            }
                        } else {
                            this.ShowToast(getString(R.string.file_not_exist))
                        }
                    }
                    finish()
                }
            }
        }
    }

    fun getFilePathToMediaID(songPath: String, context: Context): Long {
        var id: Long = 0
        val cr = context.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val selection = MediaStore.Audio.Media.DATA
        val selectionArgs = arrayOf(songPath)
        val projection = arrayOf(MediaStore.Audio.Media._ID)
//        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor = cr.query(
            uri, projection, "$selection=?", selectionArgs, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                id = cursor.getString(idIndex).toLong()
            }
        }
        return id
    }

    /**
     * Delete file.
     *
     *
     * If [ContentResolver] failed to delete the file, use trick,
     * SDK version is >= 29(Q)? use [SecurityException] and again request for delete.
     * SDK version is >= 30(R)? use [MediaStore.createDeleteRequest].
     */
    private fun delete(launcher: ActivityResultLauncher<IntentSenderRequest>, uri: Uri) {

        val contentResolver: ContentResolver = contentResolver
        try {
            //delete object using resolver
            contentResolver.delete(uri, null, null)

        } catch (e: SecurityException) {
            Log.e("Exception==>", "SecurityException :::${e}")
            var pendingIntent: PendingIntent? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val collection: ArrayList<Uri> = ArrayList()
                collection.add(uri)
                pendingIntent = MediaStore.createDeleteRequest(contentResolver, collection)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                //if exception is recoverable then again send delete request using intent
                if (e is RecoverableSecurityException) {
                    Log.e("Exception==>", "RecoverableSecurityException :::${e}")

                    pendingIntent = e.userAction.actionIntent
                }
            }
            if (pendingIntent != null) {
                val sender = pendingIntent.intentSender
                val request = IntentSenderRequest.Builder(sender).build()
                launcher.launch(request)
            }
        }
    }
}