package com.paulabravic.nextwatchapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulabravic.nextwatchapp.data.model.MovieDetailsResponse
import com.paulabravic.nextwatchapp.data.repo.MovieRepository
import com.paulabravic.nextwatchapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(var mrepo: MovieRepository) : ViewModel() {

    private val _movieDetailsFromApi = MutableLiveData<Resource<MovieDetailsResponse>>()
    val movieDetailsFromApi: LiveData<Resource<MovieDetailsResponse>>
        get() = _movieDetailsFromApi

    private val _movieDetailsFromRoom = MutableLiveData<MovieDetailsResponse>()
    val movieDetailsFromRoom: LiveData<MovieDetailsResponse>
        get() = _movieDetailsFromRoom


    fun showMovieDetailsFromRoom(id: String) {
        viewModelScope.launch {
            _movieDetailsFromRoom.postValue(mrepo.observeMovieDetails(id))
        }
    }

    fun showMovieDetailsFromApi(id: String) {
        viewModelScope.launch {
            _movieDetailsFromApi.postValue(mrepo.showMovieDetails(id))
        }
    }


}