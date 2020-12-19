package com.example.lazyloadapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lazyloadapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var mainAdapter: MainAdapter
    private val mViewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentMainBinding
    private var mObserver: Observer<List<ItemBean>>? = null
    private var dataObserver: Observer<List<ItemBean>>? = null
    private var mIsFirstObserver = true
    private val REQUEST_DATA = 10000

    private var mIsVisibleToUser = false

    private val mDelayHandler =object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == REQUEST_DATA) {
                loadData()
            }
        }
    }

    private var mainViewModel: MainViewModel? = null

    //下边这种是   by activityViewModels<>()的具体实现
//    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(
//            requireActivity().viewModelStore,
//            requireActivity().defaultViewModelProviderFactory
//        ).get(MainViewModel::class.java)
//    }

    companion object {
        fun getInstance(showText: String, id: Int): MainFragment {
            Log.d(TAG, "getInstance: showText === $showText")
            val fragment = MainFragment()
            val bundle = Bundle()
            bundle.putString("showText", showText)
            bundle.putInt("id", id)
            fragment.arguments = bundle
            return fragment
        }

        const val TAG = "MainFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_main, container, false)
        if (mainViewModel == null) {
            mainViewModel = ViewModelProvider(
                this
            ).get(MainViewModel::class.java)
            binding.viewModel = mViewModel
            binding.lifecycleOwner = this
        }

        arguments?.getString("showText")?.let {
            mainViewModel?.initShowText(it)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        var times = 0
        mObserver = Observer<List<ItemBean>> { t ->
            times++
            t?.let { itemList ->
                mainAdapter.setData(itemList)
            }

        }

    }

    override fun onResume() {
        super.onResume()
        mDelayHandler.sendEmptyMessageDelayed(REQUEST_DATA, 1200)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: === ")
        mDelayHandler.removeCallbacksAndMessages(null)

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: === ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: === ")
    }

    private fun loadData() {
        Log.d(TAG, "loadData: start")
        mainViewModel?.getTestData(requireArguments().getInt("id"))?.observe(this, mObserver!!)
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()
        binding.mainRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerview.adapter = mainAdapter
    }
}