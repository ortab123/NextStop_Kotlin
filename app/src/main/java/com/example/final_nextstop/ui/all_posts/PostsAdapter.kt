package com.example.final_nextstop.ui.all_posts

import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.databinding.PostLayoutBinding

class PostsAdapter(private val callBack:PostListener) : RecyclerView.Adapter<> {

    interface PostListener{
        fun onPostClicked(index:Int)
        fun onPostLongClicked(index:Int)
    }

    inner class PostViewHolder(private val binding:PostLayoutBinding) :
            RecyclerView.ViewHolder(binding.root){

            }
}