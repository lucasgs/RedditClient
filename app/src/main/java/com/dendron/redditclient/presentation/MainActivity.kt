package com.dendron.redditclient.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dendron.redditclient.databinding.ActivityMainBinding
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.util.loadImage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by inject()

    private val postAdapter by lazy { PostAdapter(callback) }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val callback = object : PostAdapter.Callback {
        override fun onThumbnailTapped(post: Post) {
            post.image?.let { image ->
                openPicture(image)
            }
        }

        override fun onPostTapped(post: Post) {
            viewModel.markPostAsRead(post)
        }

        override fun onPostDismissed(post: Post) {
            viewModel.dismissPost(post)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getEvents.observe(this, ::handleEvents)

        bindViews()
        fetchPosts()
    }

    private fun bindViews() {
        binding.apply {
            recycleViewPosts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = postAdapter
            }

            swipeRefreshLayout.setOnRefreshListener {
                fetchPosts()
            }

            buttonDismissAll.setOnClickListener { viewModel.dismissAll() }
        }
    }

    private fun fetchPosts() {
        viewModel.getPosts()
    }

    private fun handleEvents(state: UiState) {
        binding.swipeRefreshLayout.isRefreshing = false
        when (state) {
            is UiState.Error -> Log.e("", state.message)
            is UiState.Load -> loadPosts(state.posts)
            is UiState.PostDismissed -> {
                postAdapter.setPostDeleted(state.post)
                cleanPostDetails()
            }
            is UiState.PostRead -> {
                postAdapter.markPostAsRead(state.post)
                showPostDetails(state.post)
            }
            UiState.AllDismissed -> {
                postAdapter.dismissAll()
                cleanPostDetails()
            }
        }
    }

    private fun cleanPostDetails() {
        binding.apply {
            textViewAuthor.text = ""
            imageViewImage.loadImage(null)
            textViewTitle.text = ""
        }
    }

    private fun showPostDetails(post: Post) {
        binding.apply {
            textViewAuthor.text = post.author
            imageViewImage.loadImage(post.image)
            textViewTitle.text = post.title
        }
    }

    private fun loadPosts(posts: List<Post>) {
        postAdapter.setItem(posts)
    }

    private fun openPicture(imagePath: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(imagePath), "image/*")
        startActivity(intent)
    }


}