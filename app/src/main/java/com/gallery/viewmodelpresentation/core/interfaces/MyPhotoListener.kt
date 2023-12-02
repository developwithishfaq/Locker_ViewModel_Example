package com.gallery.viewmodelpresentation.core.interfaces

interface MyPhotoListener {
    fun onDelete(position: Int){}
    fun onLockIt(dest: String, position: Int){}
}