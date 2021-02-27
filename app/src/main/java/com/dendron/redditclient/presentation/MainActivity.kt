package com.dendron.redditclient.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dendron.redditclient.databinding.ActivityMainBinding
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.presentation.PostViewModel.Companion.POST_LIMIT
import com.dendron.redditclient.util.collectFlow
import com.dendron.redditclient.util.loadImage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by inject()

    private val postAdapter by lazy { PostAdapter(callback) }

    private lateinit var postLayoutManager: LinearLayoutManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            recycleViewPosts.apply {
                postLayoutManager = LinearLayoutManager(context)
                layoutManager = postLayoutManager
                adapter = postAdapter
                setHasFixedSize(true)
                addOnScrollListener(recyclerViewOnScrollListener)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshPosts()
            }

            buttonDismissAll.setOnClickListener {
                cleanPostDetails()
                viewModel.dismissAll()
            }

            lifecycleScope.collectFlow(viewModel.posts) { posts ->
                loadPosts(posts)
            }

            lifecycleScope.collectFlow(viewModel.spinner) { enable ->
                swipeRefreshLayout.isRefreshing = enable
            }

            slidingPanel.openPane()

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
        postAdapter.submitList(posts)
    }

    private fun openPicture(imagePath: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(imagePath), "image/*")
        startActivity(intent)
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount: Int = postLayoutManager.childCount
            val totalItemCount: Int = postLayoutManager.itemCount
            val firstVisibleItemPosition: Int = postLayoutManager.findFirstVisibleItemPosition()

            val shouldGetMoreResult =
                ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) &&
                        (firstVisibleItemPosition >= 0) && (totalItemCount >= POST_LIMIT))
            if (shouldGetMoreResult) {
                viewModel.fetchMorePosts()
            }
        }
    }

    private val callback = object : PostAdapter.Callback {
        override fun onThumbnailTapped(post: Post) {
            post.image?.let { image ->
                openPicture(image)
            }
        }

        override fun onPostTapped(post: Post) {
            binding.slidingPanel.closePane()
            showPostDetails(post)
            viewModel.markPostAsRead(post)
        }

        override fun onPostDismissed(post: Post) {
            cleanPostDetails()
            viewModel.dismissPost(post)
        }
    }
}