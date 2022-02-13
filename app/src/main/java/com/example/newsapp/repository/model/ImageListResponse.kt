package com.example.newsapp.repository.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
 class ImageListResponse (val list : ArrayList<ImageListResponseItem>?) : Parcelable