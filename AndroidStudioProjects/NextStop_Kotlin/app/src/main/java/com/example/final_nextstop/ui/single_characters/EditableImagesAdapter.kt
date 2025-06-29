package com.example.final_nextstop.ui.single_characters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import com.example.final_nextstop.databinding.ImageInEditDescriptionLayoutBinding


class EditableImagesAdapter(
    private val images: MutableList<String>,
    private val onRemoveClick: (position: Int) -> Unit

) : RecyclerView.Adapter<EditableImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ImageInEditDescriptionLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val imageUrl = images[position]

            Glide.with(binding.root.context)
                .load(imageUrl)
                .fitCenter()
                .into(binding.editDescriptionImageView)


            binding.btnRemoveImage.setOnClickListener {
                onRemoveClick(position)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageInEditDescriptionLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = images.size
}
