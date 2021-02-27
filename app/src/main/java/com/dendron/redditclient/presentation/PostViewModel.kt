package com.dendron.redditclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

sealed class UiState {
    data class Error(val message: String) : UiState()
    data class PostDismissed(val post: Post) : UiState()
    data class PostRead(val post: Post) : UiState()
    object AllDismissed : UiState()
    object LoadingFinished : UiState()
}

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    val posts: Flow<List<Post>> get() = postRepository.getPosts()

    private val _events = MutableSharedFlow<UiState>()
    val events: SharedFlow<UiState> get() = _events

    fun refreshPosts(limit: Int = 10) {
        viewModelScope.launch {
            postRepository.refreshPosts(limit)
            _events.emit(UiState.LoadingFinished)
        }
    }

    fun dismissPost(post: Post) {
        viewModelScope.launch {
            postRepository.dismissPost(post)
            _events.emit(UiState.PostDismissed(post))
        }
    }

    fun markPostAsRead(post: Post) {
        viewModelScope.launch {
            _events.emit(UiState.PostRead(post))
        }
    }

    fun dismissAll() {
        viewModelScope.launch {
            postRepository.dismissAll()
            _events.emit(UiState.AllDismissed)
        }
    }
}