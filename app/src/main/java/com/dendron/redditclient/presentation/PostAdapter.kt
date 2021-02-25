package com.dendron.redditclient.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dendron.redditclient.R
import com.dendron.redditclient.domain.model.Post

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val items = mutableListOf<Post>()

    fun setItem(newItems: List<Post>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = items[position]
        holder.setPost(post)
    }

    override fun getItemCount(): Int = items.size
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        private val textViewHours: TextView = itemView.findViewById(R.id.textViewHours)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewComments: TextView = itemView.findViewById(R.id.textViewComments)
        private val imageViewThumbnail: ImageView = itemView.findViewById(R.id.imageViewThumbnail)

        fun setPost(post: Post) {
            imageViewThumbnail.loadImage(post.thumbnail)
            textViewAuthor.text = post.author
            textViewHours.text = post.created.toString()
            textViewTitle.text = post.title
            textViewComments.text = itemView.context.resources.getQuantityString(
                R.plurals.comments_number_text,
                post.comments,
                post.comments
            )
        }
    }
}

fun ImageView.loadImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_no_image)
        .placeholder(R.drawable.ic_no_image)
        .into(this)
}