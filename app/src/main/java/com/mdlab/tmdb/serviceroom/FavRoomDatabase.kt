package com.mdlab.tmdb.serviceroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Favorite::class), version = 1, exportSchema = false)
abstract class FavRoomDatabase : RoomDatabase(){
    abstract fun favDAO(): FavDAO

    companion object{
        @Volatile
        private var INSTANCE: FavRoomDatabase? = null

        fun getDatabase(context: Context): FavRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavRoomDatabase::class.java,
                    "fav_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}