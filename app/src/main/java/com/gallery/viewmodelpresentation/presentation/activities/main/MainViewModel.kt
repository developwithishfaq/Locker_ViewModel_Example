package com.gallery.viewmodelpresentation.presentation.activities.main

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.viewModelScope
import com.gallery.viewmodelpresentation.core.interfaces.MyPhotoListener
import com.gallery.viewmodelpresentation.domain.repository.GalleryOperations
import com.gallery.viewmodelpresentation.domain.use_case.DeleteExternalStoragePhotoUseCase
import com.gallery.viewmodelpresentation.presentation.activities.main.comonents.MainScreenEvents
import com.gallery.viewmodelpresentation.presentation.activities.main.comonents.MainScreenState
import com.gallery.viewmodelpresentation.presentation.base.BaseUiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val galleryRepository: GalleryOperations
) : BaseUiViewModel<MainScreenEvents>() {

    private val mutableState = MutableStateFlow(MainScreenState())
    val state = mutableState.asStateFlow()

    private var deleteLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    fun init(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        deleteLauncher = launcher
    }

    val listener = object : MyPhotoListener {
        override fun onDelete(position: Int) {
            onEvent(MainScreenEvents.Delete(position))
        }

        override fun onLockIt(dest: String, position: Int) {
            onEvent(MainScreenEvents.Lock(dest, position))
        }
    }

    private fun deletePhoto(pos: Int) {
        val prevState = mutableState.value
        val uri = prevState.galleryPhotosList[pos].uri
        viewModelScope.launch {
            deleteLauncher?.let {
                galleryRepository.deletePhoto(
                    it, uri,
                    object : DeleteExternalStoragePhotoUseCase.DeleteCallback {
                        override fun onDelete() {
                            val list = prevState.galleryPhotosList.toMutableList()
                            list.removeAt(pos)
                            mutableState.update { screen ->
                                screen.copy(
                                    galleryPhotosList = list
                                )
                            }
                        }

                        override fun onCancel() {

                        }
                    })
            }
        }
    }

    private fun movePhoto(pos: Int, destination: String) {
        val prevState = mutableState.value
        val model = prevState.galleryPhotosList[pos]
        viewModelScope.launch {
            deleteLauncher?.let {
                val moved = galleryRepository.movePhoto(model.path, destination)
                if (moved) {
                    galleryRepository.deletePhoto(
                        deleteLauncher!!,
                        model.uri,
                        object : DeleteExternalStoragePhotoUseCase.DeleteCallback {
                            override fun onDelete() {
                                val list = prevState.galleryPhotosList.toMutableList()
                                list.removeAt(pos)
                                mutableState.update {
                                    it.copy(
                                        galleryPhotosList = list
                                    )
                                }
                            }

                            override fun onCancel() {

                            }
                        })
                }
            }
        }
    }

    private fun loadGalleryPhotos() {
        val prevList = mutableState.value.galleryPhotosList
        viewModelScope.launch {
            val list = galleryRepository.loadGalleryPhotos()
            if (prevList.isEmpty() || prevList.size != list.size) {
                mutableState.update {
                    it.copy(
                        galleryPhotosList = list
                    )
                }
            }
        }
    }

    override fun onEvent(event: MainScreenEvents) {
        when (event) {
            is MainScreenEvents.Delete -> {
                deletePhoto(event.position)
            }

            is MainScreenEvents.LauncherResults -> {
                galleryRepository.onLauncherResults(event.result)
            }

            MainScreenEvents.LoadPhotos -> {
                loadGalleryPhotos()
            }

            is MainScreenEvents.Lock -> {
                movePhoto(event.position, event.destPath)
            }
        }
    }
}



