package com.dendron.redditclient.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dendron.redditclient.databinding.ActivityMainBinding
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.util.collectFlow
import com.dendron.redditclient.util.loadImage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by inject()

    private val postAdapter by lazy { PostAdapter(callback) }

    private lateinit var binding: ActivityMainBinding

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

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            lifecycleScope.collectFlow(viewModel.posts) { posts ->
                loadPosts(posts)
            }

            lifecycleScope.collectFlow(viewModel.events) { event ->

                handleEvents(event)

            }

            recycleViewPosts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = postAdapter
            }

            swipeRefreshLayout.setOnRefreshListener {
                fetchPosts()
            }

            buttonDismissAll.setOnClickListener { viewModel.dismissAll() }
        }

        //setContentView(binding.root)
        //bindViewModels()
        //bindViews()
        fetchPosts()
    }

    private fun bindViewModels() {
        //viewModel.getEvents.observe(this, ::handleEvents)
        //viewModel.posts.observe(this, {
        //    binding.swipeRefreshLayout.isRefreshing = false
        //    loadPosts(it)
        //})
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
        viewModel.refreshPosts()
    }

    private fun handleEvents(event: UiState) {
        binding.swipeRefreshLayout.isRefreshing = false

        when (event) {
            is UiState.LoadingFinished -> binding.swipeRefreshLayout.isRefreshing = false
            is UiState.Error -> Log.e(TAG, event.message)
            is UiState.PostDismissed -> {
                postAdapter.setPostDeleted(event.post)
                cleanPostDetails()
            }
            is UiState.PostRead -> {
                postAdapter.markPostAsRead(event.post)
                showPostDetails(event.post)
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

    companion object {
        val TAG = ::MainActivity.name
    }
}