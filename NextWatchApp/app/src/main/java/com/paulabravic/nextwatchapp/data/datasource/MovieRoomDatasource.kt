package com.paulabravic.nextwatchapp.data.datasource

import androidx.lifecycle.LiveData
import com.paulabravic.nextwatchapp.data.model.Movie
import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse
import com.paulabravic.nextwatchapp.room.MovieRoomDao

class MovieRoomDatasource(private var movieRoomDao: MovieRoomDao) {

    suspend fun insertMovie(movie: Movie) {
        movieRoomDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieRoomDao.deleteMovie(movie)
    }

    fun observeMovies(): LiveData<List<Movie>> {
        return movieRoomDao.observeMovies()
    }

    suspend fun insertMovieDetails(movieDetailsResponse: MovieDetailsResponse) {
        movieRoomDao.insertMovieDetails(movieDetailsResponse)
    }

    suspend fun deleteMovieDetails(movieDetailsResponse: MovieDetailsResponse) {
        movieRoomDao.deleteMovieDetails(movieDetailsResponse)
    }

    suspend fun observeMovieDetails(id: String): MovieDetailsResponse {
        return movieRoomDao.observeMovieDetails(id)
    }

    suspend fun deleteMovieByID(id: String) {
        movieRoomDao.deleteMovieByID(id)
    }

}