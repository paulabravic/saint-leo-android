package com.paulabravic.nextwatchapp.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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
        "tt1375666", // Inception
        "tt0120338", // Titanic
        "tt3315342", // Logan
        "tt1099212", // Twilight
        "tt0217869", // Unbreakable
        "tt0099653", // Ghost
        "tt5817168", // Terminator
        "tt0393109", // The Dark Knight (2008)
        "tt1854614", // Oppenheimer (2023)
        "tt0454876", // The Avengers (2012)
        "tt0076759", // Star Wars: Episode IV - A New Hope (1977)
        "tt0266543", // Jaws (1975)
        "tt0435761", // The Social Network (2010)
        "tt1201607", // The Wolf of Wall Street (2013)
        "tt0816692", // Interstellar (2014)
        "tt0088763",  // Raiders of the Lost Ark (1981)
        "tt1848556", // The Revenant (2015)
        "tt1630029", // The Martian (2015)
        "tt2267998", // Inside Out (2015)
        "tt6751668", // Parasite (2019)
        "tt8108198", // Jojo Rabbit (2019)
        "tt1838556", // The Big Lebowski (1998)
        "tt0078788", // A Clockwork Orange (1971)
        "tt0081398", // The Shining (1980)
        "tt0111161",  // The Shawshank Redemption (1994)
        "tt0109830", // Forrest Gump (1994)
        "tt0137523", // Fight Club (1999)
        "tt0425112", // The Dark Knight Rises (2012)
        "tt0133093", // The Matrix (1999)
        "tt0097576", // The Untouchables (1987)
        "tt0110912", // Se7en (1995)
        "tt0167404", // American Beauty (1999)
        "tt1798684", // Mad Max: Fury Road (2015)
        "tt0086190", // Scarface (1983)
        "tt0105695", // Schindler's List (1993)
        "tt0075148", // Rocky (1976)
        "tt0783233", // The Hangover (2009)
        "tt0241527", // Gladiator (2000)
        "tt1386726", // Whiplash (2014)
        "tt1431045", // Interstellar (2014)
        "tt1411697", // The Big Short (2015)
        "tt5071412", // Coco (2017)
        "tt1568911", // Mad Max: Fury Road (2015)
        "tt0114369", // The Silence of the Lambs (1991)
        "tt0120815", // The Lord of the Rings: The Fellowship of the Ring (2001)
        "tt1130884", // The Grand Budapest Hotel (2014)
        "tt2181931", // Spider-Man: Into the Spider-Verse (2018)
        "tt0082971", // The Empire Strikes Back (1980)
        "tt0181689", // Memento (2000)
        "tt0209144", // The Matrix Reloaded (2003)
        "tt0304141", // The Prestige (2006)
        "tt1490017", // Django Unchained (2012)
        "tt0120586", // Saving Private Ryan (1998)
        "tt0407887", // No Country for Old Men (2007)
        "tt0133135", // Goodfellas (1990)
        "tt0848228", // Avengers: Endgame (2019)
        "tt1856101", // Dune (2021)
        "tt0412277", // The Pursuit of Happyness (2006)
        "tt0482571", // The Dark Knight (2008)
        "tt0107290", // Jurassic Park (1993)
        "tt0457430", // The Social Network (2010)
        "tt0095016",  // The Princess Bride (1987)
        "tt0081505", // The Godfather: Part II (1974)
        "tt0038650", // It's a Wonderful Life (1946)
        "tt0245429", // Spirited Away (2001)
        "tt0071315", // One Flew Over the Cuckoo's Nest (1975)
        "tt0112573", // Braveheart (1995)
        "tt0289848", // Kill Bill: Vol. 1 (2003)
        "tt0078748", // Alien (1979)
        "tt0107048", // The Lion King (1994)
        "tt0088247", // Raging Bull (1980)
        "tt0144084", // The Sixth Sense (1999)
        "tt0151817", // L.A. Confidential (1997)
        "tt0101410", // The Terminator (1984)
        "tt0099685", // Back to the Future (1985)
        "tt0280778", // City of God (2002)
        "tt0232500", // Memento (2000)
        "tt0032551", // Citizen Kane (1941)
        "tt0105236", // The Usual Suspects (1995)
        "tt0047478"  // Casablanca (1942)
    )

    var index = 1

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
