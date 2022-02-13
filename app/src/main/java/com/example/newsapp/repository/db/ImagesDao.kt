package com.example.newsapp.repository.db

import androidx.room.*
import com.example.newsapp.repository.model.Article

@Dao
interface ImagesDao {



    @Query("SELECT * FROM FavArticlesTable")
    fun findAll(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(article: Article)

    @Delete
    fun remove(article: Article)


}