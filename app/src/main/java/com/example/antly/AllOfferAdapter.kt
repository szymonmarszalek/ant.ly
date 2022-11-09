package com.example.antly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse

class AllOfferAdapter(private val allOffers: List<OfferResponse>) :
RecyclerView.Adapter<AllOfferAdapter.ViewHolder>(){

    private val allOffersList = mutableListOf<OfferResponse>()
    @SuppressLint("NotifyDataSetChanged")
    fun setOfferList(allOffers: List<OfferResponse>) {
        if(allOffersList.isNotEmpty())
            allOffersList.clear()

        allOffersList.addAll(allOffers)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(offer: OfferResponse) {
            view.findViewById<TextView>(R.id.subjectTitleTextView).text = offer.title
            view.findViewById<TextView>(R.id.subjectCategoryTextView).text = offer.subject
            view.findViewById<TextView>(R.id.priceTextView).text = offer.price.toString() + " zł"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.offer_element,
            parent,
            false
        )

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allOffers[position])
    }

    override fun getItemCount(): Int {
        return allOffers.size
    }
}