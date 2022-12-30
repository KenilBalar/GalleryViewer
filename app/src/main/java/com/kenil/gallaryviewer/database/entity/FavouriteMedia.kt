package com.kenil.gallaryviewer.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * This is Entity class for defining table and also data model class for storing data
 */
@Parcelize
@Entity(tableName = "favourite")
data class FavouriteMedia(

    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "path") var path: String?,
    @ColumnInfo(name = "media_type") var mediaType: String?,
) : Parcelable {}
