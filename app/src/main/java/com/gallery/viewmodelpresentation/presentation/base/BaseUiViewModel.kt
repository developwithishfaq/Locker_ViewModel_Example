package com.gallery.viewmodelpresentation.presentation.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class BaseUiViewModel<ScreenEvents> : ViewModel() {
    abstract fun onEvent(event: ScreenEvents)
}



