package com.gallery.viewmodelpresentation.domain.util.path_helper.components

sealed class StorageAreaType {
    data class Private(val mediaType: MediaType) : StorageAreaType()
    data class Public(val mediaType: MediaType) : StorageAreaType()
}
