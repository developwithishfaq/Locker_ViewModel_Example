package com.gallery.viewmodelpresentation.presentation.activities.vault

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.gallery.viewmodelpresentation.core.interfaces.MyPhotoListener
import com.gallery.viewmodelpresentation.core.interfaces.ToastListener
import com.gallery.viewmodelpresentation.domain.repository.VaultRepository
import com.gallery.viewmodelpresentation.presentation.activities.vault.components.VaultScreenEvents
import com.gallery.viewmodelpresentation.presentation.activities.vault.components.VaultScreenState
import com.gallery.viewmodelpresentation.presentation.base.BaseUiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultScreenViewModel @Inject constructor(
    private val repository: VaultRepository
) : BaseUiViewModel<VaultScreenEvents>() {

    private val mutableState = MutableStateFlow(VaultScreenState())
    val state = mutableState.asStateFlow()

    private var toastListener: ToastListener? = null

    val listener = object : MyPhotoListener {
        override fun onDelete(position: Int) {
            onEvent(VaultScreenEvents.Delete(position))
        }

        override fun onLockIt(dest: String, position: Int) {
            onEvent(VaultScreenEvents.Export(position, dest))
        }
    }

    private fun loadPhotos(context: Context) {
        viewModelScope.launch {
            val list = repository.fetchFiles(context.filesDir)
            mutableState.update {
                it.copy(
                    vaultPhotos = list
                )
            }

        }
    }


    override fun onEvent(event: VaultScreenEvents) {
        when (event) {
            is VaultScreenEvents.Export -> {
                exportFile(event.pos, event.dest)
            }

            is VaultScreenEvents.LoadPhotos -> {
                loadPhotos(event.context)
            }

            is VaultScreenEvents.Delete -> {
                deleteThisFile(event.pos)
            }
        }
    }

    private fun deleteThisFile(pos: Int) {
        val prevState = state.value
        val model = prevState.vaultPhotos[pos]
        viewModelScope.launch {
            val deleted = repository.deleteFile(model)
            if (deleted) {
                val list = prevState.vaultPhotos.toMutableList()
                list.removeAt(pos)
                mutableState.update {
                    it.copy(
                        vaultPhotos = list
                    )
                }
            }
        }
    }

    fun setToastListener(listener: ToastListener) {
        toastListener = listener
    }

    private fun exportFile(pos: Int, dest: String) {
        val prevState = state.value
        val model = prevState.vaultPhotos[pos]
        viewModelScope.launch {
            val moved = repository.moveFiles(model.path, dest, false)
            if (moved) {
                toastListener?.onToast("Photo Unlocked")
            }
        }
    }
}