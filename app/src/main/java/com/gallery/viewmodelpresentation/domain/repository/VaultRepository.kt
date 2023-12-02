package com.gallery.viewmodelpresentation.domain.repository

import java.io.File

interface VaultRepository {

    suspend fun fetchFiles(file: File): List<File>
    suspend fun moveFiles(path: String, dest: String, deleteOrigin: Boolean): Boolean
    suspend fun deleteFile(file: File): Boolean
}