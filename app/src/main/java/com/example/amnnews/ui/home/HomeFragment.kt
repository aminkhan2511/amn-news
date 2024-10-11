package com.example.amnnews.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.data.remote.model.TopNew
import com.example.amnnews.ui.main.MainActivity
import com.example.amnnews.ui.main.NewsViewModelMain
import com.example.amnnews.util.Resource


// TODO: Rename parameter arguments, choose names that match

class HomeFragment : Fragment() {


    lateinit var newsViewModelMain: NewsViewModelMain
    lateinit var breakingNewsAdapterHome: BreakingNewsAdapterHome
    lateinit var recyclerViewNews: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as? MainActivity)?.showBottomNavAndToolbar()

        newsViewModelMain = (activity as MainActivity).viewModel
        setupRecyclerView(view)

//        breakingNewsAdapterHome.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//
//
////            findNavController().navigate(
////                R.id.action_homeFragment_to_articleFragment,
////                bundle
////            )
//        }

        newsViewModelMain.breakingNews.observe(viewLifecycleOwner, Observer {response ->


            when(response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->


                      if (newsResponse.top_news.isNotEmpty())
                        {
                           collectAllNews(newsResponse.top_news)
                        }

                    }
                }
                is Resource.Error -> Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                is Resource.Loading -> {
                    Toast.makeText(activity,"Loading data...",Toast.LENGTH_SHORT).show()
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
        breakingNewsAdapterHome.differ.submitList(allNewsList)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView(view: View) {
        breakingNewsAdapterHome = BreakingNewsAdapterHome(::showArticleData, deleteArticle = null,requireContext())



        recyclerViewNews = view.findViewById<RecyclerView>(R.id.newsRecyclerView)

        recyclerViewNews.apply {
            adapter=breakingNewsAdapterHome
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
//            val snapHelper = PagerSnapHelper()
//            snapHelper.attachToRecyclerView(this)
        }


    }

    fun showArticleData(new: New):Unit
    {
        val bundle = Bundle().apply {
            putParcelable("article", new)
        }


            findNavController().navigate(
                R.id.action_homeFragment_to_articleFragment,
                bundle
            )
    }

}