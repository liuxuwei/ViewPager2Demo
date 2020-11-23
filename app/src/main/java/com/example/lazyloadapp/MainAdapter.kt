package com.example.lazyloadapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lazyloadapp.databinding.ItemRecommendViewBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var mBeanList: MutableList<ItemBean> = mutableListOf()

    inner class MainViewHolder(private val binding: ItemRecommendViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getBinding(): ItemRecommendViewBinding{
            return binding
        }
    }

    fun setData(dataList: List<ItemBean>) {
        Log.d(MainFragment.TAG, "setData: size is ${dataList.size}")
        if (mBeanList.size != 0) {
            mBeanList.clear()
        }
        mBeanList.addAll(dataList)
        notifyItemRangeChanged(0, dataList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemBinding = DataBindingUtil
            .inflate<ItemRecommendViewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_recommend_view, parent, false
            )
        return MainViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return mBeanList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.getBinding().itemBean = mBeanList[position]
    }
}