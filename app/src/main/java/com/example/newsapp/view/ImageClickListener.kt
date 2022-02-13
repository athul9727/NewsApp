package com.example.newsapp.view

import com.example.newsapp.repository.model.Article


interface ImageClickListener {
    fun onItemClick(article : Article)
    fun onBookMarkClick(article : Article)
}