package com.example.amnnews.ui.common

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.example.amnnews.ui.main.MainActivity
import com.example.amnnews.ui.main.NewsViewModelMain
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ArticleFragment : Fragment() {



    lateinit var viewModel: NewsViewModelMain


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel


//        fab.setOnClickListener {
//            viewModel.saveArticle(article)
//            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?


    {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_article, container, false)


        val article = arguments?.getParcelable("article") as? New

          arguments?.getString("SavGon")?.let {
              var viewSaved= view.findViewById<FloatingActionButton>(R.id.fabSavedNews)

              viewSaved.visibility = View.GONE
          }

        view.findViewById<WebView>(R.id.webView).apply {
            webViewClient = WebViewClient()
            if (article != null) {
                article.url?.let { loadUrl(it) }
            }
        }

        val fabSavedNews: View = view.findViewById(R.id.fabSavedNews)
        fabSavedNews.setOnClickListener { view ->

            GlobalScope.launch {
                if (article != null) {
                    viewModel.savedNews(article)
                }
            }
            Snackbar.make(view, "Article Saved ", Snackbar.LENGTH_SHORT).show()
        }


        return view
    }
}