package com.alland.myapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alland.myapplication.R
import com.alland.myapplication.adapter.HeadlineNewsAdapter
import com.alland.myapplication.adapter.LoadingStateAdapter
import com.alland.myapplication.adapter.NewsAdapter
import com.alland.myapplication.databinding.ActivityMainBinding
import com.alland.myapplication.ui.ViewModelFactory
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    private lateinit var adapterHeadline: HeadlineNewsAdapter
    private lateinit var adapterAll: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_swipe_refresh)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()
        initNews()
        initSwipeToRefresh()

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == View.NO_ID) {
                group.check(group.getChildAt(0).id)
                viewModel.getAllNews(filterAll())
            } else {
                viewModel.getAllNews(binding.chipGroup.findViewById<Chip>(checkedId).text.toString())
            }
        }

    }

    private fun getFilter(): String {
        val chipGroup = binding.chipGroup
        val checkedChip = chipGroup.checkedChipId

        return if (checkedChip == R.id.chip_all) {
            filterAll()
        } else {
            chipGroup.findViewById<Chip>(checkedChip).text.toString()
        }

    }
    private fun filterAll(): String {
        val chipGroup = binding.chipGroup
        val chips = chipGroup.children
        val filterList = mutableListOf<String>()

        for (chip in chips) {
            if (chip.id == R.id.chip_all) continue
            val chipView = chipGroup.findViewById<Chip>(chip.id)
            filterList.add(chipView.text.toString())
        }

        val filterText = filterList.joinToString(" OR ")

        return filterText
    }

    private fun initNews() {
        viewModel.allNews.observe(this) {
            adapterAll.submitData(lifecycle, it)
            adapterAll.notifyDataSetChanged()
        }

        viewModel.headlineNews.observe(this) {
            adapterHeadline.submitData(lifecycle, it)
            adapterHeadline.notifyDataSetChanged()
        }

        viewModel.getAllNews(filterAll())
        viewModel.getHeadlineNews()

        //loading state all news
        lifecycleScope.launch {
            adapterAll.loadStateFlow.collectLatest { loadStates ->
                when (val currentState = loadStates.refresh) {
                    is LoadState.NotLoading -> {
                        binding.shimmerLayoutAllNews.stopShimmer()
                        binding.rvAllNews.visibility = View.VISIBLE
                        binding.shimmerLayoutAllNews.visibility = View.GONE
                    }

                    is LoadState.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            currentState.error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    LoadState.Loading -> {
                        binding.shimmerLayoutAllNews.startShimmer()
                        binding.shimmerLayoutAllNews.visibility = View.VISIBLE
                        binding.rvAllNews.visibility = View.GONE
                    }
                }
            }
        }

        //loading state headline news
        lifecycleScope.launch {
            adapterHeadline.loadStateFlow.collectLatest { loadStates ->
                when (val currentState = loadStates.refresh) {
                    is LoadState.NotLoading -> {
                        binding.shimmerLayoutHeadlineNews.stopShimmer()
                        binding.rvHeadlineNews.visibility = View.VISIBLE
                        binding.shimmerLayoutHeadlineNews.visibility = View.GONE
                    }

                    is LoadState.Error -> {
                        Toast.makeText(
                            this@MainActivity,
                            currentState.error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    LoadState.Loading -> {
                        binding.shimmerLayoutHeadlineNews.startShimmer()
                        binding.shimmerLayoutHeadlineNews.visibility = View.VISIBLE
                        binding.rvHeadlineNews.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {

        //setup rv headline
        binding.rvHeadlineNews.setHasFixedSize(true)
        adapterHeadline = HeadlineNewsAdapter {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra(WebActivity.EXTRA_URL, it.url)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvHeadlineNews.layoutManager = layoutManager
        binding.rvHeadlineNews.adapter = adapterHeadline

        //setup rv all news
        adapterAll = NewsAdapter {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra(WebActivity.EXTRA_URL, it.url)
            startActivity(intent)
        }

        val layoutManagerAll = LinearLayoutManager(this)
        layoutManagerAll.orientation = LinearLayoutManager.VERTICAL

        binding.rvAllNews.layoutManager = layoutManagerAll
        binding.rvAllNews.adapter = adapterAll.withLoadStateFooter(
            LoadingStateAdapter {
                adapterAll.retry()
            }
        )
    }

    private fun initSwipeToRefresh(){
        binding.mainSwipeRefresh.setProgressViewOffset(false, 50, 100)

        binding.mainSwipeRefresh.setOnRefreshListener {
            viewModel.getHeadlineNews()
            viewModel.getAllNews(getFilter())
            binding.mainSwipeRefresh.isRefreshing = false
        }

        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.mainSwipeRefresh.isEnabled = verticalOffset == 0
        }
    }


}