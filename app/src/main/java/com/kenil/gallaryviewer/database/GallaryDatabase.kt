package com.kenil.gallaryviewer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kenil.gallaryviewer.database.dao.FavouriteDao
import com.kenil.gallaryviewer.database.entity.FavouriteMedia

/**
 * This is Singleton Database class for creating single instance of Room Db
 */

@Database(
    entities = [FavouriteMedia::class], version = 1
)
abstract class GallaryDatabase : RoomDatabase() {

    abstract fun getFavouriteDao(): FavouriteDao

    companion object {
        private var Instance: GallaryDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): GallaryDatabase {
            if (Instance == null) {
                Instance = Room.databaseBuilder(
                    context, GallaryDatabase::class.java, "GALLARY_DATABASE"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return Instance!!
        }
    }
}