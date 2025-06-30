package com.example.final_nextstop.ui.all_characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_nextstop.R
import com.example.final_nextstop.data.model.Post
import com.example.final_nextstop.databinding.PostLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(val callback: PostListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val posts = ArrayList<Post>()

    fun setPosts(newPosts: Collection<Post>){
        this.posts.clear()
        this.posts.addAll(newPosts)
        notifyDataSetChanged()
    }

    interface PostListener {
        fun onPostClicked(index:Int)
        fun onFavoriteClicked(post: Post)
    }

    inner class PostViewHolder(private val binding: PostLayoutBinding)
        :RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callback.onPostClicked(adapterPosition)
        }

        fun bind(post: Post){
            binding.txtViewPostLocation.text = ""
            binding.txtViewPostPlace.text = ""
            binding.txtViewPostDescription.text = ""
            binding.textViewDatePost.text = ""
            binding.postImage.setImageDrawable(null)

            binding.txtViewPostLocation.text = post.location
            binding.txtViewPostPlace.text = post.place
            binding.txtViewPostDescription.text = post.description
            binding.textViewDatePost.text = SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(post.date)

            val images = post.images
            if (!images.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(images[0])
                    .circleCrop()
                    .into(binding.postImage)
            } else {
                binding.postImage.setImageDrawable(null)
            }

            val isFav = post.isFavorite
            binding.btnFavorite.setImageResource(
                if (isFav) R.drawable.heart_filled else R.drawable.heart_outline
            )
            binding.btnFavorite.setOnClickListener {
                callback.onFavoriteClicked(post)
            }
        }
    }


    fun postAt(position: Int) = posts[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostViewHolder(PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) =
        holder.bind(posts[position])

    override fun getItemCount() =
        posts.size
}