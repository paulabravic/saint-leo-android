package com.paulabravic.nextwatchapp.data.datasource

import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse
import com.paulabravic.nextwatchapp.retrofit.MovieDao
import com.paulabravic.nextwatchapp.util.Resource

class MovieDataSource(private var mdao: MovieDao) {

    suspend fun showMovieDetails(id: String): Resource<MovieDetailsResponse> {
        return try {
            val response = mdao.showMovieDetails(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }

}