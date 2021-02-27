package com.dendron.redditclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    val posts: Flow<List<Post>> get() = postRepository.getPosts()

    private val _spinner = MutableStateFlow(true)
    val spinner: StateFlow<Boolean> get() = _spinner

    var after: String? = null

    init {
        refreshPosts()
    }

    fun refreshPosts() {
        _spinner.value = true
        try {
            fetchMorePosts()
        } finally {
            _spinner.value = false
        }
    }

    fun fetchMorePosts() {
        viewModelScope.launch {
            when (val result = postRepository.fetchMorePosts(POST_LIMIT, after ?: "")) {
                is ResultWrapper.Error -> Unit
                is ResultWrapper.Success -> after = result.nextPage
            }
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

    companion object {
        const val POST_LIMIT = 10
    }

}