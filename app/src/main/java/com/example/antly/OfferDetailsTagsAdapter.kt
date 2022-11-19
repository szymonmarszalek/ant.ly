package com.example.antly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class OfferDetailsTagsAdapter(private val allTags: List<String>) :
RecyclerView.Adapter<OfferDetailsTagsAdapter.ViewHolder>(){

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(tag: String) {
            view.findViewById<TextView>(R.id.tagTextView).text = tag.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.offer_details_tag_element,
            parent,
            false
        )

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allTags[position])
    }

    override fun getItemCount(): Int {
        return allTags.size
    }
}