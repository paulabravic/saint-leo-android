package com.paulabravic.nextwatchapp.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.paulabravic.nextwatchapp.data.model.Movie
import com.paulabravic.nextwatchapp.data.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(private var mrepo: MovieRepository) : ViewModel() {

    private val _viewState = MutableStateFlow(MovieListViewState())
    val viewState: StateFlow<MovieListViewState> = _viewState.asStateFlow()

    private val imdbList = listOf(
        "tt0096895", // Batman (1989)
        "tt0372784", // Batman Begins (2005)
        "tt1877830", // The Batman (2022)
        "tt1375666", // Inception (2010)
        "tt0120338", // Titanic (1997)
        "tt3315342", // Logan (2017)
        "tt1099212", // Twilight (2008)
        "tt0217869", // Unbreakable (2000)
        "tt0099653", // Ghost (1990)
        "tt6450804", // Terminator: Dark Fate (2019)
        "tt0468569", // The Dark Knight (2008)
        "tt15398776", // Oppenheimer (2023)
        "tt0848228", // The Avengers (2012)
        "tt0076759", // Star Wars: Episode IV - A New Hope (1977)
        "tt0073195", // Jaws (1975)
        "tt1285016", // The Social Network (2010)
        "tt0993846", // The Wolf of Wall Street (2013)
        "tt0816692", // Interstellar (2014)
        "tt0082971", // Raiders of the Lost Ark (1981)
        "tt1663202", // The Revenant (2015)
        "tt3659388", // The Martian (2015)
        "tt2096673", // Inside Out (2015)
        "tt6751668", // Parasite (2019) ️
        "tt2584384", // Jojo Rabbit (2019)
        "tt0118715", // The Big Lebowski (1998)
        "tt0066921", // A Clockwork Orange (1971)
        "tt0081505", // The Shining (1980)
        "tt0111161", // The Shawshank Redemption (1994) ️
        "tt0109830", // Forrest Gump (1994)
        "tt0137523", // Fight Club (1999)
        "tt1345836", // The Dark Knight Rises (2012)
        "tt0133093", // The Matrix (1999)
        "tt0094226", // The Untouchables (1987)
        "tt0114369", // Se7en (1995)
        "tt0169547", // American Beauty (1999)
        "tt1392190", // Mad Max: Fury Road (2015)
        "tt0086250", // Scarface (1983)
        "tt0108052", // Schindler's List (1993)
        "tt0075148", // Rocky (1976) ️
        "tt1119646", // The Hangover (2009)
        "tt0172495", // Gladiator (2000)
        "tt2582802", // Whiplash (2014)
        "tt0816692", // Interstellar (2014)
        "tt1596363", // The Big Short (2015)
        "tt2380307", // Coco (2017)
        "tt1392190", // Mad Max: Fury Road (2015)
        "tt0102926", // The Silence of the Lambs (1991)
        "tt0120737", // The Lord of the Rings: The Fellowship of the Ring (2001)
        "tt2278388", // The Grand Budapest Hotel (2014)
        "tt4633694", // Spider-Man: Into the Spider-Verse (2018)
        "tt0080684", // The Empire Strikes Back (1980)
        "tt0209144", // Memento (2000) ️
        "tt0234215", // The Matrix Reloaded (2003)
        "tt0482571", // The Prestige (2006)
        "tt1853728", // Django Unchained (2012)
        "tt0120815", // Saving Private Ryan (1998)
        "tt0477348", // No Country for Old Men (2007)
        "tt0099685", // Goodfellas (1990)
        "tt4154796", // Avengers: Endgame (2019)
        "tt1160419", // Dune (2021)
        "tt0454921", // The Pursuit of Happyness (2006)
        "tt0468569", // The Dark Knight (2008)
        "tt0107290", // Jurassic Park (1993) ️
        "tt1285016", // The Social Network (2010)
        "tt0093779", // The Princess Bride (1987)
        "tt0071562", // The Godfather: Part II (1974)
        "tt0038650", // It's a Wonderful Life (1946) ️
        "tt0245429", // Spirited Away (2001) ️
        "tt0073486", // One Flew Over the Cuckoo's Nest (1975)
        "tt0112573", // Braveheart (1995) ️
        "tt0266697", // Kill Bill: Vol. 1 (2003)
        "tt0078748", // Alien (1979)
        "tt0110357", // The Lion King (1994)
        "tt0081398", // Raging Bull (1980)
        "tt0167404", // The Sixth Sense (1999)
        "tt0119488", // L.A. Confidential (1997)
        "tt0088247", // The Terminator (1984)
        "tt0088763", // Back to the Future (1985)
        "tt0317248", // City of God (2002)
        "tt0033467", // Citizen Kane (1941)
        "tt0114814", // The Usual Suspects (1995)
        "tt0034583"  // Casablanca (1942)
    )

    var index = 1
    val roomMovieList = mrepo.getMovies()

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        mrepo.insertMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        mrepo.deleteMovie(movie)
    }

    fun insertMovieDetails(movie: Movie) {
        viewModelScope.launch {
            val movieDetailsResponse = mrepo.showMovieDetails(movie.id).data
            if (movieDetailsResponse != null) {
                mrepo.insertMovieDetails(movieDetailsResponse)
            }
        }
    }


    fun deleteMovieDetails(movie: Movie) {
        viewModelScope.launch {
            val movieDetailsResponse = mrepo.showMovieDetails(movie.id).data
            if (movieDetailsResponse != null) {
                mrepo.deleteMovieDetails(movieDetailsResponse)
            }
        }
    }

    fun fetchMoviesByImdb() {
        viewModelScope.launch {
            val randomImdbIds = imdbList.shuffled().take(3)

            val uiModelList = randomImdbIds.mapIndexed { index, imdbId ->
                val movieDetails = mrepo.showMovieDetails(imdbId).data

                UiModel.RepoItem(
                    movie = Movie(
                        title = movieDetails?.title ?: "",
                        year = movieDetails?.year ?: "",
                        id = movieDetails?.id ?: "",
                        type = "movie",
                        posterPath = movieDetails?.poster ?: ""
                    ),
                    index = index + 1
                )
            }

            _viewState.value = _viewState.value.copy(movieResponse = flowOf(PagingData.from(uiModelList)))
        }
    }

}


data class MovieListViewState(
    val movieResponse: Flow<PagingData<UiModel>>? = null,
)


sealed class UiModel {
    data class RepoItem(val movie: Movie, val index: Int) : UiModel()
    data class SeparatorItem(val pageNum: Int) : UiModel()
}
