package com.paulabravic.nextwatchapp.di

import android.content.Context
import androidx.room.Room
import com.paulabravic.nextwatchapp.data.datasource.MovieDataSource
import com.paulabravic.nextwatchapp.data.datasource.MovieRoomDatasource
import com.paulabravic.nextwatchapp.data.repo.MovieRepository
import com.paulabravic.nextwatchapp.retrofit.ApiUtils
import com.paulabravic.nextwatchapp.retrofit.MovieDao
import com.paulabravic.nextwatchapp.room.MovieRoomDao
import com.paulabravic.nextwatchapp.room.MovieRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(mds: MovieDataSource, movieRoomDatasource: MovieRoomDatasource): MovieRepository {
        return MovieRepository(mds, movieRoomDatasource)
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

    @Provides
    @Singleton
    fun provideMovieRoomDataSource(movieRoomDao: MovieRoomDao): MovieRoomDatasource {
        return MovieRoomDatasource(movieRoomDao)
    }

    @Provides
    @Singleton
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MovieRoomDatabase::class.java, "movies").fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun injectDao(database: MovieRoomDatabase) = database.movieRoomDao()

}