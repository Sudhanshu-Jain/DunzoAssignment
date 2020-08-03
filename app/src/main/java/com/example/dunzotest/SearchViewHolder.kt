package com.example.dunzotest

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


public class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var searchImage: ImageView = view.findViewById(R.id.img_search)
    var searchText: TextView = view.findViewById(R.id.search_txt)



}