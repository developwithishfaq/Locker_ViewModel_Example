package com.gallery.viewmodelpresentation.presentation.activities.main.comonents

import androidx.activity.result.ActivityResult

sealed class MainScreenEvents {
    data object LoadPhotos : MainScreenEvents()
    data class Delete(val position: Int) : MainScreenEvents()
    data class Lock(val destPath :String,val position: Int) : MainScreenEvents()
    data class LauncherResults(val result: ActivityResult) : MainScreenEvents()
}

