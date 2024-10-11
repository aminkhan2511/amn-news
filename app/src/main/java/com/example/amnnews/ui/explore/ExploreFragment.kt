package com.example.amnnews.ui.explore

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.ui.home.BreakingNewsAdapterHome
import com.example.amnnews.ui.main.MainActivity
import com.example.amnnews.ui.main.NewsViewModelMain
import com.example.amnnews.util.Resource


// TODO: Rename parameter arguments, choose names that match

class ExploreFragment : Fragment() {


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
        var view = inflater.inflate(R.layout.fragment_explore, container, false)



        newsViewModelMain = (activity as MainActivity).viewModel
        setupRecyclerView(view)
        setupSearchView(view)



        newsViewModelMain.searchNews.observe(viewLifecycleOwner, Observer {response ->


            when(response) {
                is Resource.Success -> {
                    response.data?.let { newsResponse ->


                        if (newsResponse.news.isNotEmpty())
                        {
                            collectAllNews(newsResponse.news)
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

    private fun collectAllNews(topNews: List<New>) {


        breakingNewsAdapterHome.differ.submitList(topNews)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView(view: View) {
        breakingNewsAdapterHome = BreakingNewsAdapterHome(::showArticleData, deleteArticle = null,requireContext())



        recyclerViewNews = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerViewNews.apply {
            adapter=breakingNewsAdapterHome
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)

        }


    }

    fun showArticleData(new: New):Unit
    {
        val bundle = Bundle().apply {
            putParcelable("article", new)
        }


        findNavController().navigate(
            R.id.action_exploreFragment_to_articleFragment,
            bundle
        )
    }

    private  fun setupSearchView(view:View) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Handle before text changes
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                // Handle text changes
                Log.d("Amin",query.toString())
                query?.let { if (query.toString().length>=3)
                {
                    newsViewModelMain.searchNewsSuspend(query.toString())
                }
                    Log.d("Amin",query.toString())}

            }

            override fun afterTextChanged(s: Editable?) {
                // Handle after text changes
            }
        }

        view.findViewById<EditText>(R.id.txtSearch).addTextChangedListener(textWatcher)


    }

}