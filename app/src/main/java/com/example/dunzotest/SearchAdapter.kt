package com.example.dunzotest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dunzotest.data.PhotoModel
import java.lang.StringBuilder
import kotlin.properties.Delegates


class SearchAdapter() : RecyclerView.Adapter<SearchViewHolder>() {

    var items: List<PhotoModel> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val v: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): PhotoModel{
        return items[position]
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.searchText.setText(items[position].title)
        Glide.with(holder.itemView).load(buildImageUrl(items[position])).centerCrop().into(holder.searchImage)
    }

    private fun buildImageUrl(model: PhotoModel) :String {
        val sb = StringBuilder()
        sb.append("https://farm")
        sb.append(model.farm)
        sb.append(".staticflickr.com/")
        sb.append(model.server)
        sb.append("/" + model.id + "_" + model.secret + "_m.jpg")
        return sb.toString()
    }
}