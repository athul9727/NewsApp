package com.example.newsapp.viewmodel

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.repository.Repository
import com.example.newsapp.repository.model.Article
import com.example.newsapp.util.AppResult
import com.example.newsapp.util.ConnectivityLiveData
import com.example.newsapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository,
    private val application: Application) : ViewModel() {

    val showLoading = ObservableBoolean()
    val imageList = MutableLiveData<List<Article>>()
    val bookmarkList = MutableLiveData<List<Article>>()
    val showError = SingleLiveEvent<String>()
    val connectivityLiveData = ConnectivityLiveData(application)

    val bttext = "Search"
    val inputtext = MutableLiveData<String>()
    val page = MutableLiveData<Int>()
    private val statusmessage = SingleLiveEvent<String>()
    val message: SingleLiveEvent<String>
        get() {  return statusmessage }



    fun getalldata(str:String,page:Int) {
        showLoading.set(true)
        viewModelScope.launch {

            val result =  repository.getAlldata(str,page.toString())
            showLoading.set(false)



            when (result) {
                is AppResult.Success -> {
                    imageList.value = result.successData.articles
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }

//    fun getmoredata(str:String) {
//        getalldata(inputtext.value.toString(), page)
//    }


    fun getallbookmarkdata() {
        showLoading.set(true)
        viewModelScope.launch {

            val result =  repository.getAllBookMarkdata()
            showLoading.set(false)


            when (result) {
                is AppResult.Success -> {
                    bookmarkList.value = result.successData
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }

    fun adddata(article:Article) {
        viewModelScope.launch {
            repository.adddata(article)
        }
    }

    fun removedata(article:Article) {
        viewModelScope.launch {
            repository.removedata(article)
        }
    }


    fun set_edittext() {

        if (inputtext.value == null) {
            statusmessage.value = "Please enter text"
        } else {

            page.value = 1
            getalldata(inputtext.value!!, page.value!!)

        }

    }
}