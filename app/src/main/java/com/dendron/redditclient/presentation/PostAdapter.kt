package com.dendron.redditclient.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dendron.redditclient.R
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.domain.model.Status
import com.dendron.redditclient.util.loadImage
import com.dendron.redditclient.util.toRelativeTime

class PostAdapter(private val callback: Callback) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val items = mutableListOf<Post>()

    fun setItem(newItems: List<Post>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setPostDeleted(post: Post) {
        items.remove(post)
        notifyDataSetChanged()
    }

    fun markPostAsRead(post: Post) {
        val index = items.indexOf(post)
        items[index].status = Status.read
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
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        private val textViewHours: TextView = itemView.findViewById(R.id.textViewHours)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewComments: TextView = itemView.findViewById(R.id.textViewComments)
        private val imageViewThumbnail: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
        private val buttonDismissPost: Button = itemView.findViewById(R.id.buttonDismissPost)
        private val imageReadIndicator: ImageView =
            itemView.findViewById(R.id.imageViewReadIndicator)

        fun setPost(post: Post) {
            imageReadIndicator.visibility =
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

            itemView.setOnClickListener {
                callback.onPostTapped(post)
            }
            buttonDismissPost.setOnClickListener { callback.onPostDismissed(post) }
            imageViewThumbnail.setOnClickListener {
                post.thumbnail?.let {
                    callback.onThumbnailTapped(
                        post
                    )
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
