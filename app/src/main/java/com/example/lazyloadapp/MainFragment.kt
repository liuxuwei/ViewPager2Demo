package com.example.lazyloadapp

import android.os.Bundle
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
    private var dataObserver: Observer<List<ItemBean>>? = null
    private var mIsFirstObserver = true

    private var mIsVisibleToUser = false

    //下边这种是   by activityViewModels<>()的具体实现
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            requireActivity().viewModelStore,
            requireActivity().defaultViewModelProviderFactory
        ).get(MainViewModel::class.java)
    }

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
        arguments?.getString("showText")?.let {
            mainViewModel.initShowText(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getString("showText")?.let {
            Log.d(TAG, "onCreateView: showText = $it")
        }
        arguments?.getInt("id")?.let {
            Log.d(TAG, "onCreateView: ===  id = $it")
        }
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        loadData()
    }

    private fun loadData() {
        var times = 0

        mainViewModel.getTestData(requireArguments().getInt("id")).observe(this.viewLifecycleOwner, Observer<List<ItemBean>> { t ->
                times++
                Log.d(
                    TAG,
                    "onChanged: times +=== $times  current fragment title " +
                            "is ${arguments?.getString("showText")} " +
                            "id is ${arguments?.getInt("id")}"
                )
                t?.let { itemList ->
                    mainAdapter.setData(itemList)
                }

        })
    }

    private fun initAdapter() {
        mainAdapter = MainAdapter()
        binding.mainRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerview.adapter = mainAdapter
    }
}