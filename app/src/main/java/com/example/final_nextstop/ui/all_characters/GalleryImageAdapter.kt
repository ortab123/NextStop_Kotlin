package com.example.final_nextstop.ui.all_characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_nextstop.data.model.PostImage
import com.example.final_nextstop.databinding.ImageInGalleryLayoutBinding

    class GalleryImageAdapter(private val images: List<PostImage>,
                              private val onClick: (PostImage) -> Unit
) : RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {


    inner class ImageViewHolder(private val binding: ImageInGalleryLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(images[adapterPosition])
            }
        }

        fun bind(image: PostImage) {
            if (image.imageUrl.isNotBlank()) {
                Glide.with(binding.root)
                    .load(image.imageUrl)
                    .into(binding.galleryImageView)
                binding.galleryImageView.visibility = View.VISIBLE
            } else {
                binding.galleryImageView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageInGalleryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

}
