package com.paulabravic.nextwatchapp.retrofit

import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse
import com.paulabravic.nextwatchapp.data.model.MoviesResponse
import com.paulabravic.nextwatchapp.util.Consts.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieDao {

    @GET("?")
    suspend fun searchMovie(
        @Query("s")title: String,
        @Query("page")page: Int,
        @Query("apikey") apikey: String = API_KEY)
    : MoviesResponse

    @GET("?")
    suspend fun showMovieDetails(
        @Query("i")title: String,
        @Query("apikey") apikey: String = API_KEY)
            : Response<MovieDetailsResponse>
}