package com.example.bild_atr_vieta

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ImagePagerAdapter(private val images: List<File>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    class ImageViewHolder(val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bitmap = BitmapFactory.decodeFile(images[position].absolutePath)
        holder.imageView.setImageBitmap(bitmap)
    }

    override fun getItemCount() = images.size
}
