package com.gallery.viewmodelpresentation.presentation.activities.vault.components

import android.content.Context
import java.io.File

sealed class VaultScreenEvents {
    data class Delete(val pos: Int): VaultScreenEvents()
    data class Export(val pos: Int,val dest:String): VaultScreenEvents()
    data class LoadPhotos(val file: File) :VaultScreenEvents()
}