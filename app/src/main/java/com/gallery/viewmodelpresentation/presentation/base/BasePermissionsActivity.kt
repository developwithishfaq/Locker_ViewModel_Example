package com.gallery.viewmodelpresentation.presentation.base

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BasePermissionsActivity : BaseActivity() {

    enum class PermissionType {
        GALLERY_READ, GALLERY_WRITE,
    }

    private var listener: PermissionListener? = null

    interface PermissionListener {
        fun onAccepted()
        fun onDenied(){}
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                listener?.onAccepted()
            } else {
                listener?.onDenied()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun askForPermissions(permissionType: PermissionType, callback: PermissionListener) {
        listener = callback
        when (permissionType) {
            PermissionType.GALLERY_READ -> {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                launcher.launch(permissions)
            }

            PermissionType.GALLERY_WRITE -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    listener?.onAccepted()
                }
            }
        }
    }
}