package com.paulabravic.nextwatchapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paulabravic.nextwatchapp.data.model.Movie
import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse

@Database(entities = [Movie::class, MovieDetailsResponse::class], version = 4)
abstract class MovieRoomDatabase : RoomDatabase() {
    abstract fun movieRoomDao(): MovieRoomDao
}
