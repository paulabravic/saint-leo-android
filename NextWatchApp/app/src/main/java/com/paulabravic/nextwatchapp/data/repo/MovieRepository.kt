package com.paulabravic.nextwatchapp.data.repo

import com.paulabravic.nextwatchapp.data.datasource.MovieDataSource
import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse
import com.paulabravic.nextwatchapp.util.Resource

class MovieRepository(private var mds: MovieDataSource) {

    suspend fun showMovieDetails(id: String): Resource<MovieDetailsResponse> = mds.showMovieDetails(id)
}