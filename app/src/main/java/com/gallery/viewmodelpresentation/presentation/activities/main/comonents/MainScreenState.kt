package com.gallery.viewmodelpresentation.presentation.activities.main.comonents

import com.gallery.viewmodelpresentation.domain.model.PhotoModel

data class MainScreenState(
    val galleryPhotosList: List<PhotoModel> = emptyList()
)

