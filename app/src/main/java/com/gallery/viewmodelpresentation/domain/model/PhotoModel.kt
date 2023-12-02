package com.gallery.viewmodelpresentation.domain.model

import android.net.Uri

data class PhotoModel(
    val name:String,
    val path: String,
    val uri: Uri
)
