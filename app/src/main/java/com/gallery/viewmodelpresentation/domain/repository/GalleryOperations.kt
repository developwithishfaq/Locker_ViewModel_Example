package com.gallery.viewmodelpresentation.domain.repository

import android.content.AttributionSource
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.gallery.viewmodelpresentation.domain.model.PhotoModel
import com.gallery.viewmodelpresentation.domain.use_case.DeleteExternalStoragePhotoUseCase

interface GalleryOperations {
    suspend fun loadGalleryPhotos(): List<PhotoModel>
    suspend fun deletePhoto(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uri: Uri,
        callback: DeleteExternalStoragePhotoUseCase.DeleteCallback
    )

    fun onLauncherResults(result: ActivityResult)
    suspend fun movePhoto(source: String,destination:String) : Boolean
}