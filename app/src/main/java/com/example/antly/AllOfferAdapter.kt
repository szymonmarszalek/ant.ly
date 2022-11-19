package com.example.antly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.OfferResponse
import com.squareup.picasso.Picasso

class AllOfferAdapter(
    private val allOffers: List<OfferResponse>,
    private val offer: (OfferResponse) -> Unit,
) :
    RecyclerView.Adapter<AllOfferAdapter.ViewHolder>() {

    private val allOffersList = mutableListOf<OfferResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setOfferList(allOffers: List<OfferResponse>) {
        if (allOffersList.isNotEmpty())
            allOffersList.clear()

        allOffersList.addAll(allOffers)
        notifyDataSetChanged()
    }

    class ViewHolder(
        val view: View,
        private val offer: (OfferResponse) -> Unit,
    ) : RecyclerView.ViewHolder(view) {
        fun bind(offer: OfferResponse) {
            view.findViewById<TextView>(R.id.subjectTitleTextView).text = offer.subject
            view.findViewById<TextView>(R.id.subjectTopicTextView).text = offer.title
            view.findViewById<TextView>(R.id.offerLocation).text = offer.location
            view.findViewById<TextView>(R.id.offerLevel).text = offer.range
            view.findViewById<TextView>(R.id.offerPrice).text = offer.price.toString() + " £"
            Picasso
                .get()
                .load(offer.imageUrl)
                .error(R.drawable.student_icon)
                .noPlaceholder()
                .into(view.findViewById<ImageView>(R.id.offerImage))
        }

        fun clickOnOffer(offer: OfferResponse) {
            view.setOnClickListener {
                offer(offer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.offer_element,
            parent,
            false
        )

        return ViewHolder(inflatedView, offer)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allOffers[position])
        holder.clickOnOffer(allOffers[position])
    }

    override fun getItemCount(): Int {
        return allOffers.size
    }
}