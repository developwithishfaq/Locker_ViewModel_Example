package com.gallery.viewmodelpresentation.data.repository

import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.gallery.viewmodelpresentation.domain.model.PhotoModel
import com.gallery.viewmodelpresentation.domain.repository.GalleryOperations
import com.gallery.viewmodelpresentation.domain.use_case.DeleteExternalStoragePhotoUseCase
import com.gallery.viewmodelpresentation.domain.use_case.FetchGalleryPhotosUseCase
import com.gallery.viewmodelpresentation.domain.use_case.MoveFileUseCase
import javax.inject.Inject

class GalleryOperationsImpl @Inject constructor(
    private val fetchGalleryPhotosUseCase: FetchGalleryPhotosUseCase,
    private val deletePhotoUseCase: DeleteExternalStoragePhotoUseCase,
    private val moveFileUseCase: MoveFileUseCase
) : GalleryOperations {

    override suspend fun loadGalleryPhotos(): List<PhotoModel> {
        return fetchGalleryPhotosUseCase()
    }

    override suspend fun deletePhoto(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uri: Uri,
        callback: DeleteExternalStoragePhotoUseCase.DeleteCallback
    ) {
        deletePhotoUseCase(launcher, uri, callback)
    }

    override fun onLauncherResults(result: ActivityResult) {
        deletePhotoUseCase.onResults(result)
    }

    override suspend fun movePhoto(source: String, destination: String) =
        moveFileUseCase(source, destination)

}