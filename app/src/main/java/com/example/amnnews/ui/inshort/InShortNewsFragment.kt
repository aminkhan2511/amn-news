package com.example.amnnews.ui.inshort

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.data.remote.model.TopNew
import com.example.amnnews.ui.home.BreakingNewsAdapterHome
import com.example.amnnews.ui.main.NewsViewModelMain


class InShortNewsFragment : Fragment() {

    lateinit var inShortAdapter: InShortAdapter
    lateinit var recyclerViewShortNews: RecyclerView
    private val viewModel: NewsViewModelMain by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.inshort_news_fragment, container, false)

        setupRecyclerView(view)

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { it ->
            it.data?.let {newsRes->

                if (newsRes.top_news.isNotEmpty())
                {
                    collectAllNews(newsRes.top_news)
                }

            }
        })

        return view
    }
    private fun collectAllNews(topNews: List<TopNew>) {
        // Initialize an empty mutable list of New
        val allNewsList = mutableListOf<New>()

        // Loop through each TopNew item in the topNews list
        for (topNewItem in topNews) {
            // Assuming each TopNew has a property `news` that is a list of New
            allNewsList.addAll(topNewItem.news)
        }

        // Now allNewsList contains all the New items from all TopNew objects
        // You can submit this list to the adapter
        inShortAdapter.differ.submitList(allNewsList)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView(view: View) {
        inShortAdapter = InShortAdapter()



        recyclerViewShortNews = view.findViewById(R.id.inshortRecyclerView)

        with(recyclerViewShortNews) {
            adapter = inShortAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }


    }


}