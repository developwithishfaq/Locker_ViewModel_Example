package com.gallery.viewmodelpresentation.presentation.activities.vault.components

import android.content.Context

sealed class VaultScreenEvents {
    data class Delete(val pos: Int): VaultScreenEvents()
    data class Export(val pos: Int,val dest:String): VaultScreenEvents()
    data class LoadPhotos(val context: Context) :VaultScreenEvents()
}