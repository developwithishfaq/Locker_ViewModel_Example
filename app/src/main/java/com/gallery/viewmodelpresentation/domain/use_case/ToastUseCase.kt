package com.gallery.viewmodelpresentation.domain.use_case

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}