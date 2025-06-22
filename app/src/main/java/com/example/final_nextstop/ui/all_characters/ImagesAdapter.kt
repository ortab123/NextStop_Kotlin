package com.example.final_nextstop.ui.all_characters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R

class ImagesAdapter(
    private val images: MutableList<Uri?>,
    private val onAddImageClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.new_photo_image_view)

        fun bind(uri: Uri?) {
            if (uri == null) {
                // הצג את סמל הפלוס
                imageView.setImageResource(R.drawable.add_new_image)
                imageView.setPadding(30, 30, 30, 30)
                imageView.setOnClickListener {
                    onAddImageClick(adapterPosition)
                }
            } else {
                // הצג את התמונה שנבחרה
                imageView.setPadding(0, 0, 0, 0)
                imageView.setImageURI(uri)
                imageView.setOnClickListener(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_image_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
