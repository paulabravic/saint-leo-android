package com.paulabravic.nextwatchapp.di

import com.paulabravic.nextwatchapp.data.datasource.MovieDataSource
import com.paulabravic.nextwatchapp.data.repo.MovieRepository
import com.paulabravic.nextwatchapp.retrofit.ApiUtils
import com.paulabravic.nextwatchapp.retrofit.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(mds: MovieDataSource): MovieRepository {
        return MovieRepository(mds)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(mDao: MovieDao): MovieDataSource {
        return MovieDataSource(mDao)
    }

    @Provides
    @Singleton
    fun provideMoviesDao(): MovieDao {
        return ApiUtils.getMovieDao()
    }

}