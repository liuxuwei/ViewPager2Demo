package com.example.lazyloadapp

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var mShowText = ObservableField<String>()
    private val mTitleListLiveData = MutableLiveData<List<String>>()
    private val itemBeanLiveDataList = mutableListOf<MutableLiveData<List<ItemBean>>>()


    fun initShowText(text: String) {
        mShowText.set(text)
    }


    fun getTitleList(): MutableLiveData<List<String>> {
        val titleList = arrayListOf<String>()
        for (i in 0..8) {
            titleList.add("标题 $i")
        }
        mTitleListLiveData.value = titleList
        return mTitleListLiveData
    }


    fun getTestData(index: Int): MutableLiveData<List<ItemBean>> {
        val mItemBeanListLiveData = MutableLiveData<List<ItemBean>>()
        itemBeanLiveDataList.add(mItemBeanListLiveData)
        val itemList = mutableListOf<ItemBean>()
        for (i in 0..20) {
            val itemBean = ItemBean(i*index, "desc ${i*index}", "text ${i*index} === show")
            itemList.add(itemBean)
        }
        itemBeanLiveDataList[index].value = itemList

       
        return itemBeanLiveDataList[index]
    }
}