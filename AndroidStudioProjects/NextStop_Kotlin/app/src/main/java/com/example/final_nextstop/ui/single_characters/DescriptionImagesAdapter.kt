package com.example.final_nextstop.ui.single_characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_nextstop.databinding.ImagesInDescriptionLayoutBinding

class DescriptionImagesAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<DescriptionImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ImagesInDescriptionLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImagesInDescriptionLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.binding.root.context)
            .load(imageUrl)
            .fitCenter()
            .into(holder.binding.descriptionImageView)
    }

    override fun getItemCount(): Int = images.size
}
