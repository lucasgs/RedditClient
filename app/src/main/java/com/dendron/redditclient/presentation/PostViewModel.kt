package com.dendron.redditclient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.launch

sealed class UiState {
    data class Load(val posts: List<Post>): UiState()
    data class Error(val message: String): UiState()
}

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private var events = MutableLiveData<UiState>()
    val getEvents: LiveData<UiState> = events

    fun getPosts(limit: Int = 10) {

        viewModelScope.launch {
            when(val result = postRepository.getPosts(limit)) {
                is ResultWrapper.Error -> events.postValue(UiState.Error(result.message))
                is ResultWrapper.Success -> events.postValue(UiState.Load(result.data))
            }
        }
    }
}