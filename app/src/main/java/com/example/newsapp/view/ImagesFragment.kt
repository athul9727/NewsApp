package com.example.newsapp.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentImagesBinding
import com.example.newsapp.repository.model.Article
import com.example.newsapp.util.PaginationScrollListener
import com.example.newsapp.util.replaceFragment
import com.example.newsapp.view.ImageClickListener
import com.example.newsapp.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesFragment : Fragment() , ImageClickListener {

  //  private val ViewModel by viewModel<ViewModel>()
    private val viewModel: ViewModel by viewModels()
    private lateinit var recAdapter: RecAdapter
    private lateinit var mDataBinding: FragmentImagesBinding

    private var _binding: FragmentImagesBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var isLastPage: Boolean = false
    var isLoading: Boolean = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mDataBinding  = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_images,
            container,
            false)

        val mRootView = mDataBinding.root
        mDataBinding.lifecycleOwner = this
        return mRootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setView()
        removeBackButton()
        mDataBinding.viewModel = viewModel

        viewModel.inputtext.value = "apple"

        viewModel.connectivityLiveData.observe(viewLifecycleOwner, Observer { isAvailable ->
            when (isAvailable) {
                true -> {
                    if (!viewModel.inputtext.value.isNullOrEmpty()) {
                        if (viewModel.imageList.value.isNullOrEmpty()) {
                           // viewModel.getalldata(viewModel.inputtext.value.toString(), 1)
                            viewModel.getdata()
                        }
                    }
                }
                false -> {
                    viewModel.imageList.value = null
                    Toast.makeText(context,"No Network", Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.imageList.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it.isNotEmpty()) {

                    if(viewModel.page==1){
                        recAdapter.setlist(it)
                    }
                    else{
                        //after getting your data you have to assign false to isLoading
                        isLoading = false
                        recAdapter.addmoreData(it)
                    }

                }
            }

        })


        viewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
        })




    }

    private fun removeBackButton() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as? AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(false)
    }

    private fun setView() {
        recAdapter = RecAdapter(context, this)
        mDataBinding.recycleview.layoutManager = LinearLayoutManager(activity)
        mDataBinding.recycleview.setHasFixedSize(true)
        // recycleview.setItemViewCacheSize(12)
        mDataBinding.recycleview.adapter = recAdapter

        mDataBinding.recycleview.addOnScrollListener(object : PaginationScrollListener(
            mDataBinding.recycleview.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                Log.e("paging","loading more items")
                viewModel.getdata()

            }

        })
    }

//    fun getMoreItems() {
//        Log.e("paging","loading more items")
//        viewModel.getdata()
//        //after getting your data you have to assign false to isLoading
//        isLoading = false
//
//    }

    override fun onItemClick(article : Article) {
        (activity as MainActivity).replaceFragment(
            ImageDetailsFragment.newInstance(article),
            R.id.fragment_container, "imagedetails")
    }

    override fun onBookMarkClick(article: Article) {
        viewModel.adddata(article)
    }



}