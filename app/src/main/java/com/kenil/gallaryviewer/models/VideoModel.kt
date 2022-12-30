package com.kenil.gallaryviewer.models

data class VideoModel(
    var path : String? = null ?: "",
    var thumb : String? = null ?: "",
    var isSelected : Boolean = false
)
