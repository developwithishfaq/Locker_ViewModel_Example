package com.gallery.viewmodelpresentation.domain.util.constants

import java.io.File


fun String.toFile() = File(this)
fun File.toFile() = this