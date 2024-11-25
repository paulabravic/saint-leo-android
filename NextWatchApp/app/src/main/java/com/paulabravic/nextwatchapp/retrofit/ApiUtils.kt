package com.paulabravic.nextwatchapp.retrofit

import com.paulabravic.nextwatchapp.util.Consts.BASE_URL

class ApiUtils {
    companion object {
        fun getMovieDao(): MovieDao {
            return RetrofitClient.getClient(BASE_URL).create(MovieDao::class.java)
        }
    }
}
