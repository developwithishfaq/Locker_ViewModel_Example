package com.gallery.viewmodelpresentation.domain.use_case

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class DeleteFileUseCase @Inject constructor() {
    suspend operator fun invoke(file: File): Boolean {
        return withContext(Dispatchers.IO) {
            if (file.exists()) {
                if (file.isFile) {
                    file.delete()
                } else {
                    false
                }
            } else {
                false
            }
        }

    }
}