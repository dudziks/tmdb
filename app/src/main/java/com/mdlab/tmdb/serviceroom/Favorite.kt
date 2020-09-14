package com.mdlab.tmdb.serviceroom

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Only favourite movies ids are stored
 */
@Entity(tableName = "fav_table")
data class Favorite(
    @PrimaryKey val id: Int
)