package com.gallery.viewmodelpresentation.domain.use_case

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteExternalStoragePhotoUseCase @Inject constructor(
    private val resolver: ContentResolver,
    private val coroutineScope: CoroutineScope
) {

    private var photoUri: List<Uri> = emptyList()
    private var listener: DeleteCallback? = null
    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    interface DeleteCallback {
        fun onDelete()
        fun onCancel()
    }

    suspend operator fun invoke(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        uri: Uri,
        callback: DeleteCallback
    ) {
        this.launcher = launcher
        this.listener = callback
        photoUri = listOf(uri)
        deletePhotoFromExternalStorage()
    }


    fun onResults(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                coroutineScope.launch {
                    deletePhotoFromExternalStorage(true)
                }
            }
            listener?.onDelete()
        } else {
            listener?.onCancel()
        }
    }

    private suspend fun deletePhotoFromExternalStorage(fromLauncher: Boolean = false) {
        return withContext(Dispatchers.IO) {
            try {
                var id = -1
                photoUri.forEach { uri ->
                    id = resolver.delete(uri, null, null)
                }
                if (id != -1) {
                    if (!fromLauncher) {
                        listener?.onDelete()
                    }
                }
            } catch (e: SecurityException) {
                try {
                    val intentSender = when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                            MediaStore.createDeleteRequest(
                                resolver, photoUri
                            ).intentSender
                        }

                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            val recoverableSecurityException = e as? RecoverableSecurityException
                            recoverableSecurityException?.userAction?.actionIntent?.intentSender
                        }

                        else -> null
                    }
                    intentSender?.let {
                        launcher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }
                } catch (e: Exception) {

                }
            }
        }
    }
}