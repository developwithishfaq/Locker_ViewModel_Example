package com.gallery.viewmodelpresentation.domain.util.path_helper

import android.content.Context
import android.os.Environment
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.MediaType
import com.gallery.viewmodelpresentation.domain.util.path_helper.components.StorageAreaType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PathHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {


    fun getPathWithFileName(fileNameWithExtension: String, path: StorageAreaType): String {
        return getPath(path).path + "/${fileNameWithExtension}"
    }

    fun getPath(storageAreaType: StorageAreaType): File {
        var publicRootPath = Environment.getExternalStorageDirectory().path + "/Pictures/My Vault/"
        var privateRootPath = context.filesDir.path + "/"
        return when (storageAreaType) {
            is StorageAreaType.Private -> {
                privateRootPath += storageAreaType.mediaType.toName()
                File(privateRootPath).apply {
                    mkdirs()
                }
            }

            is StorageAreaType.Public -> {
                publicRootPath += storageAreaType.mediaType.toName()
                File(publicRootPath).apply {
                    try {
                        mkdirs()
                    } catch (_: Exception) {

                    }
                }
            }
        }
    }

    fun getPrivatePhotosFile(): File {
        return getPath(StorageAreaType.Private(MediaType.PHOTOS))
    }

    fun getPrivateVideosFile(): File {
        return getPath(StorageAreaType.Private(MediaType.VIDEOS))
    }

    fun getPublicPhotosFile(): File {
        return getPath(StorageAreaType.Public(MediaType.PHOTOS))
    }

    fun getPublicVideosFile(): File {
        return getPath(StorageAreaType.Public(MediaType.VIDEOS))
    }


    private fun MediaType.toName(): String {
        return when (this) {
            MediaType.PHOTOS -> {
                "Photos"
            }

            MediaType.VIDEOS -> {
                "Videos"
            }

            MediaType.AUDIOS -> {
                "Audios"
            }
        }
    }

}