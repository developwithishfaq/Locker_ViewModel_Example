package com.gallery.viewmodelpresentation.presentation.base

import androidx.lifecycle.ViewModel
import com.gallery.viewmodelpresentation.domain.use_case.ToastUseCase

abstract class BaseUiViewModel<ScreenEvents> : ViewModel() {
    abstract fun onEvent(event: ScreenEvents)
}



