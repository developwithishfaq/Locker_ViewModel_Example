package com.gallery.viewmodelpresentation.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.gallery.viewmodelpresentation.core.interfaces.MyPhotoListener
import com.gallery.viewmodelpresentation.databinding.PhotosRowBinding
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.MediaType
import com.gallery.viewmodelpresentation.domain.util.path_helper.PathHelper
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.StorageAreaType
import java.io.File
import javax.inject.Inject

private val mCallback = object : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.path == newItem.path
    }

}

class PhotoFilesAdapter @Inject constructor(
    private val glide: RequestManager,
    private val pathHelper: PathHelper
) : ListAdapter<File, PhotoFilesAdapter.MyViewHolder>(mCallback) {

    private var mListener: MyPhotoListener? = null

    fun setListener(callBack: MyPhotoListener) {
        mListener = callBack
    }

    inner class MyViewHolder(val binding: PhotosRowBinding) : ViewHolder(binding.root) {
        init {
            binding.lockIcon.setOnClickListener {
                val pos = adapterPosition
                if (pos != -1) {
                    val model = getItem(pos)
                    val dest = pathHelper.getPathWithFileName(
                        model.name,
                        StorageAreaType.Public(MediaType.PHOTOS)
                    )
                    mListener?.onLockIt(dest, pos)
                }
            }
            binding.deleteIcon.setOnClickListener {
                val pos = adapterPosition
                if (pos != -1) {
                    mListener?.onDelete(pos)
                }
            }
        }

        fun bindItems(file: File) {
            binding.lockIcon.text = buildString {
                append("Unlock Photo")
            }
            binding.fileNameTv.text = file.name
            glide.load(file.path).centerCrop().into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PhotosRowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(getItem(position))
    }
}