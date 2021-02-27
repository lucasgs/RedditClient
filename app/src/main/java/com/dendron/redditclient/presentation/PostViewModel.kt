package com.dendron.redditclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    val posts: Flow<List<Post>> get() = postRepository.getPosts()

    private val _spinner = MutableStateFlow(true)
    val spinner: StateFlow<Boolean> get() = _spinner

    fun refreshPosts(limit: Int = 10) {
        viewModelScope.launch {
            _spinner.value = true
            postRepository.refreshPosts(limit)
            _spinner.value = false
        }
    }

    fun markPostAsRead(post: Post) {
        viewModelScope.launch {
            postRepository.markPostAsRead(post)
        }
    }

    fun dismissPost(post: Post) {
        viewModelScope.launch {
            postRepository.dismissPost(post)
        }
    }

    fun dismissAll() {
        viewModelScope.launch {
            postRepository.dismissAll()
        }
    }
}