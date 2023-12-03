package com.gallery.viewmodelpresentation.domain.use_case

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import javax.inject.Inject

class MoveFileUseCase @Inject constructor(
) {

    suspend operator fun invoke(
        source: String,
        destination: String,
        deleteOriginal: Boolean = false
    ): Boolean {
        return moveFile(source, destination, deleteOriginal)
    }

    private suspend fun moveFile(
        pathSource: String,
        pathDestination: String,
        deleteOriginal: Boolean = false
    ): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d("moveFile", "Path Source: $pathSource")
            Log.d("moveFile", "Path Destination: $pathDestination")
            val fileSource = File(pathSource)
            try {
                if (fileSource.exists()) {
                    val fileDestination = File(pathDestination)
                    if (!fileDestination.exists()) {
                        fileDestination.createNewFile()
                    }
                    var source: FileChannel? = null
                    var destination: FileChannel? = null
                    source = FileInputStream(fileSource).channel
                    destination = FileOutputStream(fileDestination).channel
                    var count: Long = 0
                    val size = source.size()
                    while (destination.transferFrom(source, count, size - count)
                            .let { count += it; count } < size
                    )
                        source.close()
                    destination.close()

                    if (fileDestination.exists() && deleteOriginal && fileSource.exists()) {
                        fileSource.delete()
                    }
                    try {
                    } catch (_: Exception) {
                    }
                    true
                } else {
                    false
                }
            } catch (e: FileNotFoundException) {
                Log.d("cvv", "moveFile Exception: ${e.message}")
                false
            } catch (e: Exception) {
                Log.d("cvv", "moveFile Exception: ${e.message}")
                false
            }
        }
    }


}