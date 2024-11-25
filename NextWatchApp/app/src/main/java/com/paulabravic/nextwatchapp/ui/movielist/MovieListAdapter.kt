package com.paulabravic.nextwatchapp.ui.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.paulabravic.nextwatchapp.R
import com.paulabravic.nextwatchapp.data.model.Movie
import com.paulabravic.nextwatchapp.databinding.MovieListItemBinding
import com.paulabravic.nextwatchapp.databinding.MovieSeperatorBinding

class MovieListAdapter() : PagingDataAdapter<UiModel, ViewHolder>(UIMODEL_COMPARATOR) {
    var listener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (viewType == R.layout.movie_list_item) {
            val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MoviesViewHolder(binding)
        } else {
            val binding = MovieSeperatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SeparatorViewHolder(binding)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.movie_list_item
            is UiModel.SeparatorItem -> R.layout.movie_seperator
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    class MoviesViewHolder(private val binding: MovieListItemBinding) : ViewHolder(binding.root) {
        fun bind(item: UiModel.RepoItem) {
            binding.movie = item.movie
            binding.executePendingBindings()
        }
    }

    class SeparatorViewHolder(private val binding: MovieSeperatorBinding) : ViewHolder(binding.root) {
        fun bind(item: UiModel.SeparatorItem) {
            val pageNumText = item.pageNum
            binding.pageNumString = pageNumText.toString()
        }
    }


    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
                        oldItem.movie.id == newItem.movie.id) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.pageNum == newItem.pageNum)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                if (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem) {
                    return oldItem.movie == newItem.movie
                } else if (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem) {
                    return oldItem.pageNum == newItem.pageNum
                }
                return false
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.RepoItem -> {
                    (holder as MoviesViewHolder).bind(uiModel)
                }
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel)
                else -> {}
            }

        }
    }

}

interface ItemClickListener {
    fun onButtonClickDelete(item: Movie)
    fun onButtonClickInsert(item: Movie): Boolean

}
