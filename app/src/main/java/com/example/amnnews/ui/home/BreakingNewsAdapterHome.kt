package com.example.amnnews.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.amnnews.R
import com.example.amnnews.data.remote.model.New
import com.squareup.picasso.Picasso




class BreakingNewsAdapterHome(private val showArticle: ((New) -> Unit)? = null,
                              private val deleteArticle: ((New) -> Unit)? = null,
                              private val contex:Context) :
    RecyclerView.Adapter<BreakingNewsAdapterHome.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsTime: TextView
        var newsImage: ImageView
        var newsSource: TextView
        var newsTitle: TextView
        var newsDesp: TextView


        init {
            // Define click listener for the ViewHolder's View
            newsImage = view.findViewById(R.id.newsImage)
            newsTime = view.findViewById(R.id.newsTime)
            newsSource = view.findViewById(R.id.newsSource)
            newsTitle = view.findViewById(R.id.newsTitle)
            newsDesp = view.findViewById(R.id.newsDesp)
        }
    }




    val diffUtil = object : DiffUtil.ItemCallback<New>() {
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.news_design, viewGroup, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val news = differ.currentList[position]



        Log.d("NEWS_IMG", news?.url!!)

//        holder.apply {
//            Glide.with(itemView.context).load(article.urlToImage).into(newsImage)
//            newsSource.text = article.source?.name ?: "Unknown Source"
//            newsTitle.text = article.title ?: "No Title"
//            newsDesp.text = article.description ?: "No Description"
//            newsTime.text = article.publishedAt ?: "No Date"
//
//            itemView.setOnClickListener {
//                onItemClickListener?.let {
//                    it(article) }
//            }
//        }

        holder.apply {
                    Picasso.get().load(news.image).into(holder.newsImage);
                        newsDesp.text = news.title
            newsTime.text = news.publish_date
            newsSource.text = news.author
            newsTitle.text = news.summary
        }





        holder.itemView.apply {
            setOnClickListener {

                showArticle?.let { it1 -> it1(news) }

            }



        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun removeItem(position: Int) {
        // Remove the item from the datasetn
        deleteArticle?.let { it(differ.currentList[position]) }

        Toast.makeText(contex,"Article Removed",Toast.LENGTH_SHORT).show()
        val newItems = differ.currentList.toMutableList()
        newItems.removeAt(position)
        differ.submitList(newItems)



    }





}
