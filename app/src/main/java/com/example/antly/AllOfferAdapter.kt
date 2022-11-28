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
    private val offer: (OfferResponse) -> Unit,
) :
    RecyclerView.Adapter<AllOfferAdapter.ViewHolder>() {

    private var allOffersList = listOf<OfferResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setOfferList(allOffers: List<OfferResponse>) {

        allOffersList = allOffers
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
        holder.bind(allOffersList[position])
        holder.clickOnOffer(allOffersList[position])
    }

    override fun getItemCount(): Int {
        return allOffersList.size
    }
}