package com.example.newsapp.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.ListItemBinding
import com.example.newsapp.repository.model.Article



class RecAdapter(
    val context: Context?,
    val clickListener: ImageClickListener
    ) : RecyclerView.Adapter<RecAdapter.ImageViewHolder>() {

    companion object{
        private val ITEM = 0
        private val LOADING = 1
    }
    private val isLoadingAdded = false


    var list: MutableList<Article> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val mDataBinding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item, parent, false
        )
        return ImageViewHolder(mDataBinding)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun addmoreData(listItems: List<Article>) {
        val size = this.list.size
        this.list.addAll(listItems)
        val sizeNew = this.list.size
        notifyItemRangeChanged(size, sizeNew)
    }

    fun setlist(imagelist: List<Article>) {
        this.list = imagelist.toMutableList()
        notifyDataSetChanged()
    }
    fun removeitem(model: Article) {
        val position = list.indexOf(model)
        list.remove(model)
        notifyItemRemoved(position)
    }


    inner class ImageViewHolder(private val mDataBinding: ListItemBinding) :
        RecyclerView.ViewHolder(mDataBinding.root) {

        fun onBind(position: Int) {
            val row = list[position]
            mDataBinding.imagedata = row
            mDataBinding.imageClickInterface = clickListener
        }
    }

}
