package com.paulabravic.nextwatchapp.ui.movielist

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.paulabravic.nextwatchapp.data.model.Movie
import com.paulabravic.nextwatchapp.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

@AndroidEntryPoint
class MovieListFragment : Fragment(), ItemClickListener {

    private val viewModel: MovieListViewModel by viewModels()
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val movieListAdapter = MovieListAdapter()
    private val movieListViewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.movieListFragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListAdapter.listener = this
        binding.rvMovieList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieListAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter { movieListAdapter.retry() },
                footer = MovieLoadStateAdapter { movieListAdapter.retry() }
            )
        }

        if (movieListAdapter.itemCount > 0) {
            // Data is available, update UI with data
            binding.rvMovieList.isVisible = true
            binding.textViewEmpty.isVisible = false
        } else {
            // Data is not available, display empty state
            binding.rvMovieList.isVisible = false
            binding.textViewEmpty.isVisible = true
        }

        subscribeToObservers()
        observeData()
        checkStates()

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieListViewModel.viewState.collectLatest { movieViewState ->
                    movieViewState.movieResponse?.collectLatest {
                        movieListAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    }
                }
            }
        }
    }

    private fun checkStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieListAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    pbMovieList.isVisible = loadStates.source.refresh is LoadState.Loading
                    rvMovieList.isVisible = loadStates.source.refresh is LoadState.NotLoading
                    buttonRetryMain.isVisible = loadStates.source.refresh is LoadState.Error
                    textViewErrorMain.isVisible = loadStates.source.refresh is LoadState.Error
                    if (loadStates.source.refresh is LoadState.Error) {
                        var tempBool = false
                        val errorMessage = when ((loadStates.refresh as LoadState.Error).error) {
                            is SocketTimeoutException -> "The server is taking too long to respond. Please try again later."
                            is IOException -> "There was a problem connecting to the server. Please check your internet connection and try again later."
                            else -> {
                                tempBool = true
                                (loadStates.refresh as LoadState.Error).error.message
                            }
                        }
                        textViewErrorMain.text = errorMessage
                        if (tempBool) {
                            buttonRetryMain.isVisible = false
                        }
                    }
                    if (loadStates.source.refresh is LoadState.NotLoading &&
                        loadStates.append.endOfPaginationReached &&
                        movieListAdapter.itemCount < 1
                    ) {
                        rvMovieList.isVisible = false
                        textViewEmpty.isVisible = true

                    } else {
                        textViewEmpty.isVisible = false
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.index = 1
    }

    fun searchOnClick() {
        viewModel.fetchMoviesByImdb()
        viewModel.index = 1
    }

    private fun subscribeToObservers() {
    }

    override fun onButtonClickDelete(item: Movie) {
    }

    override fun onButtonClickInsert(item: Movie): Boolean {
        return if (isInternetAvailable(requireContext())) {
            true
        } else {
            false
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
