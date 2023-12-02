package com.gallery.viewmodelpresentation.data.repository

import com.gallery.viewmodelpresentation.domain.repository.VaultRepository
import com.gallery.viewmodelpresentation.domain.use_case.DeleteFileUseCase
import com.gallery.viewmodelpresentation.domain.use_case.MoveFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class VaultRepositoryImpl(
    private val moveFileUseCase: MoveFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
) : VaultRepository {
    override suspend fun fetchFiles(file: File): List<File> {
        return withContext(Dispatchers.IO) {
            file.listFiles()?.filter {
                it.isFile && it.exists() && it.name.endsWith(".jpg")
            } ?: emptyList()
        }
    }

    override suspend fun moveFiles(path: String, dest: String, deleteOrigin: Boolean): Boolean {
        return moveFileUseCase(path, dest, deleteOrigin)
    }

    override suspend fun deleteFile(file: File): Boolean {
        return deleteFileUseCase(file)
    }
}