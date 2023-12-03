package com.gallery.viewmodelpresentation.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.RequestManager
import com.gallery.viewmodelpresentation.core.interfaces.MyPhotoListener
import com.gallery.viewmodelpresentation.databinding.PhotosRowBinding
import com.gallery.viewmodelpresentation.domain.model.PhotoModel
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.MediaType
import com.gallery.viewmodelpresentation.domain.util.path_helper.PathHelper
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.StorageAreaType
import javax.inject.Inject

private val mCallback = object : DiffUtil.ItemCallback<PhotoModel>() {
    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.path == newItem.path
    }

}

class MyPhotosAdapter @Inject constructor(
    private val glide: RequestManager, private val pathHelper: PathHelper
) : ListAdapter<PhotoModel, MyPhotosAdapter.MyViewHolder>(mCallback) {

    var mListener: MyPhotoListener? = null

    fun setListener(
        callback: MyPhotoListener,
    ) {
        mListener = callback
    }


    inner class MyViewHolder(val binding: PhotosRowBinding) : ViewHolder(binding.root) {
        init {
            binding.deleteIcon.setOnClickListener {
                val pos = adapterPosition
                if (pos != -1) {
                    mListener?.onDelete(pos)
                }
            }
            binding.lockIcon.setOnClickListener {
                val pos = adapterPosition
                if (pos != -1) {
                    val model = getItem(pos)
                    val dest = pathHelper.getPathWithFileName(
                        model.name, StorageAreaType.Private(MediaType.PHOTOS)
                    )
                    mListener?.onLockIt(dest, pos)
                }
            }
        }

        fun bindItems(item: PhotoModel) {
            binding.fileNameTv.text = item.name
            glide.load(item.uri).centerCrop().into(binding.imageView)
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