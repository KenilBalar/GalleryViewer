package com.kenil.gallaryviewer.interfaces

interface Media{
    fun onMediaClick(path : String , type : String , isFromFavourite : Boolean = false)
}