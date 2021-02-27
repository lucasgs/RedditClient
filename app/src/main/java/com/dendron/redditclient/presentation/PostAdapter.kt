package com.dendron.redditclient.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dendron.redditclient.R
import com.dendron.redditclient.databinding.PostItemBinding
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.domain.model.Status
import com.dendron.redditclient.util.loadImage
import com.dendron.redditclient.util.toRelativeTime

class PostAdapter(private val callback: Callback) :
    ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = PostItemBinding.bind(itemView)

        fun bind(post: Post) {
            binding.apply {
                imageViewReadIndicator.visibility =
                    if (post.status == Status.unread) View.VISIBLE else View.INVISIBLE
                imageViewThumbnail.loadImage(post.thumbnail)
                textViewAuthor.text = post.author
                textViewHours.text = post.created.toRelativeTime()
                textViewTitle.text = post.title
                textViewComments.text = itemView.context.resources.getQuantityString(
                    R.plurals.comments_number_text,
                    post.comments,
                    post.comments
                )
                itemView.setOnClickListener { callback.onPostTapped(post) }
                buttonDismissPost.setOnClickListener { callback.onPostDismissed(post) }
                imageViewThumbnail.setOnClickListener {
                    post.thumbnail?.let { callback.onThumbnailTapped(post) }
                }
            }
        }
    }

    interface Callback {
        fun onThumbnailTapped(post: Post)
        fun onPostTapped(post: Post)
        fun onPostDismissed(post: Post)
    }
}

class DiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

