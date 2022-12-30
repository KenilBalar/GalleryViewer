package com.kenil.gallaryviewer.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kenil.gallaryviewer.database.entity.FavouriteMedia

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(favouriteMedia: FavouriteMedia)

    @Query("SELECT * FROM favourite")
    fun getFavouriteMedia(): List<FavouriteMedia>

    @Query("SELECT EXISTS(SELECT * FROM favourite WHERE path = :path)")
    fun isMediaExist(path: String?): Boolean

    @Query("DELETE FROM favourite WHERE path = :path")
    fun removeFromFavourite(path: String?)

}