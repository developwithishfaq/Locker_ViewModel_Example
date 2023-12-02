package com.gallery.viewmodelpresentation.domain.use_case

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.gallery.viewmodelpresentation.domain.model.PhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FetchGalleryPhotosUseCase @Inject constructor(
    private val resolver: ContentResolver
) {

    suspend operator fun invoke(): List<PhotoModel> {
        return loadGalleryPictures()
    }

    private suspend fun loadGalleryPictures(): List<PhotoModel> {
        val photosTempList = mutableListOf<PhotoModel>()
        return withContext(Dispatchers.IO) {
            val uri = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                }

                else -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }


            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
            )
            resolver.query(
                uri, projection, null, null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            ).use { cursor ->
                while (cursor!!.moveToNext()) {
                    val photoIdC =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val photoNameC =
                        cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                            ?: ""
                    val photoPath =
                        cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        photoIdC
                    )
                    photosTempList.add(
                        PhotoModel(
                            photoNameC,
                            photoPath ?: "",
                            contentUri,
                        )
                    )
                }
                cursor.close()
            }
            photosTempList
        }
    }
}