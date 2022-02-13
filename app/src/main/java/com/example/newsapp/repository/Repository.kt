package com.example.newsapp.repository

import android.content.Context
import android.util.Log
import com.example.newsapp.repository.db.ImagesDao
import com.example.newsapp.repository.model.Article
import com.example.newsapp.repository.model.NewsModel
import com.example.newsapp.util.AppResult
import com.example.newsapp.util.Constants
import com.example.newsapp.util.NetworkManager.isOnline
import com.example.newsapp.util.Utils.handleApiError
import com.example.newsapp.util.Utils.handleSuccess
import com.example.newsapp.util.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface Repository {

    suspend fun getAlldata(str:String,page:String) : AppResult<NewsModel>

    suspend fun adddata(article: Article)

    suspend fun removedata(article: Article)

    suspend fun getAllBookMarkdata() :  AppResult<List<Article>>

}

class RepositoryImpl @Inject constructor(
    private val api: Api,
    val context: Context,
    private val imgdao: ImagesDao
    ) :

    Repository {

    override suspend fun getAlldata(str:String,page:String): AppResult<NewsModel> {
        if (isOnline(context)) {
            return try {
                val response = api.getAlldata(str,page, Constants.api_key)
                if (response.isSuccessful) {

                    //online case...add data to db

//                    response.body()?.let {
//                        withContext(Dispatchers.IO) { imgdao.add(it) }
//                    }

                    //return data from api
                    handleSuccess(response)
                } else {
                    handleApiError(response)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {

            return context.noNetworkConnectivityError()

            //offline case...get data from db

//            val data = getImageListResponseItemFromCache()
//            return if (data.isNotEmpty()) {
//                //data not empty..so return data
//                AppResult.Success(data)
//            } else {
//                //no network
//                return context.noNetworkConnectivityError()
//            }

        }
    }

    override suspend fun adddata(article: Article) {

        withContext(Dispatchers.IO) { imgdao.add(article) }

    }

    override suspend fun removedata(article: Article) {
        withContext(Dispatchers.IO) { imgdao.remove(article) }
    }

    override suspend fun getAllBookMarkdata(): AppResult<List<Article>> {

        val data = withContext(Dispatchers.IO) {
            imgdao.findAll()
        }
        return if (data.isNotEmpty()) {
            //data not empty..so return data
            Log.e("listsize",data.size.toString())
            AppResult.Success(data)
        } else {
            //no network
            return context.noNetworkConnectivityError()
        }
    }



//    private suspend fun getImageListResponseItemFromCache(): List<ImageListResponseItem> {
//        return withContext(Dispatchers.IO) {
//            imgdao.findAll()
//        }
//    }

}




//Network
interface Api {
    @GET("/v2/everything?pageSize=20")
    suspend fun getAlldata(
        @Query("q") searchstr:String,
        @Query("page") page:String,
        @Query("apiKey") apikey:String
    ): Response<NewsModel>
}