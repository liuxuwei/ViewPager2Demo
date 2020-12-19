package com.example.lazyloadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lazyloadapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: FragmentStateAdapter
    private var mFragmentList: MutableList<Fragment> = mutableListOf()
    private lateinit var mBinding: ActivityMainBinding

    override fun getViewModelStore(): ViewModelStore {
        return super.getViewModelStore()
    }

    private val mViewModel: MainViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory
            .getInstance(application).create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = mViewModel

        initTabLayout()
        initViewPager()
    }

    private fun initTabLayout() {
        mBinding.mainTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    mBinding.mainViewpager.setCurrentItem(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        mViewModel.getTitleList().observe(this, Observer { titleList ->
            createFragments(titleList)
        })
    }

    private fun createFragments(titles: List<String>) {
        for (i in titles) {
            mBinding.mainTab.addTab(mBinding.mainTab.newTab().setText(i))
            val fragment = MainFragment.getInstance(i, titles.indexOf(i))
            mFragmentList.add(fragment)
        }
    }

    private fun initViewPager() {
        pagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragmentList[position]
            }
        }
        mBinding.mainViewpager.adapter = pagerAdapter
//        val tabLayoutMediator = TabLayoutMediator(mBinding.mainTab, mBinding.mainViewpager, object : TabLayoutMediator.TabConfigurationStrategy{
//            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
//                Log.d(Companion.TAG, "onConfigureTab: ===")
//            }
//        })
//
//        tabLayoutMediator.attach()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}