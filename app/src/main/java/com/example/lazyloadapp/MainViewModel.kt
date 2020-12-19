package com.example.lazyloadapp

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lazyloadapp.MainFragment.Companion.TAG
import java.lang.Exception

class MainViewModel : ViewModel() {
    var mShowText = ObservableField<String>()
    private val mTitleListLiveData = MutableLiveData<List<String>>()
    val itemBeanLiveData: MutableLiveData<List<ItemBean>> = MutableLiveData()
    private val mDelayHandler = object : Handler(Looper.getMainLooper()) {

    }


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
        val itemList = mutableListOf<ItemBean>()
        for (i in 0..20) {
            val itemBean = ItemBean(i * index, "desc ${i * index}", "text ${i * index} === show")
            itemList.add(itemBean)
        }
        mDelayHandler.postDelayed({
            itemBeanLiveData.value = (itemList)
        }, 2000)



        return itemBeanLiveData
    }
}