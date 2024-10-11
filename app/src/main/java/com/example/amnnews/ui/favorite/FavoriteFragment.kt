package com.example.amnnews.ui.favorite

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.ui.home.BreakingNewsAdapterHome
import com.example.amnnews.ui.main.MainActivity
import com.example.amnnews.ui.main.NewsViewModelMain
import com.example.amnnews.util.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match

class FavoriteFragment : Fragment() {


    lateinit var viewModel: NewsViewModelMain
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



        viewModel = (activity as MainActivity).viewModel
        bringSavedNews()
        setupRecyclerView(view)


        viewModel.savedNewsListRoom.observe(viewLifecycleOwner, Observer{

            breakingNewsAdapterHome.differ.submitList(it.reversed())

        })


        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun bringSavedNews() {

       GlobalScope.launch {  viewModel.getSavedNewsListAll() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView(view: View) {
        breakingNewsAdapterHome = BreakingNewsAdapterHome(::showArticleData,::deleteSavedArticle,requireContext())

        recyclerViewNews = view.findViewById<RecyclerView>(R.id.newsRecyclerView)

        recyclerViewNews.apply {
            adapter = breakingNewsAdapterHome
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//            val snapHelper = PagerSnapHelper()
//            snapHelper.attachToRecyclerView(this)



            // Attach ItemTouchHelper
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(breakingNewsAdapterHome))
            itemTouchHelper.attachToRecyclerView(recyclerViewNews)
        }


    }
    override fun onResume() {
        super.onResume()
        // Ensure scrolling to the top when the fragment is resumed
        recyclerViewNews.post {
            recyclerViewNews.scrollToPosition(0)
        }
    }
    fun showArticleData(new: New): Unit {

        val bundle = Bundle().apply {
            putParcelable("article", new)
            putString("SavGon","Gone")
        }


        findNavController().navigate(
            R.id.action_favoriteFragment_to_articleFragment,
            bundle
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteSavedArticle(article: New)
    {
       GlobalScope.launch {  viewModel.deleteArticle(article) }
    }
}