package com.example.antly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.OfferResponse
import com.squareup.picasso.Picasso

class AddedOfferAdapter(
    private val offer: (OfferResponse) -> Unit,
    private val editOffer: (OfferResponse) -> Unit,
    private val deleteOffer: (OfferResponse) -> Unit
) :
    RecyclerView.Adapter<AddedOfferAdapter.ViewHolder>() {

    private val addedOfferList = mutableListOf<OfferResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setAddedOfferList(allOffers: List<OfferResponse>) {
        if (addedOfferList.isNotEmpty())
            addedOfferList.clear()

        addedOfferList.addAll(allOffers)
        notifyDataSetChanged()
    }

    class ViewHolder(
        val view: View,
        private val offer: (OfferResponse) -> Unit,
        private val editOffer: (OfferResponse) -> Unit,
        private val deleteOffer: (OfferResponse) -> Unit,
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
            view.findViewById<ImageButton>(R.id.deleteOfferButton).setOnClickListener {
                deleteOffer(offer)
            }

            view.findViewById<ImageButton>(R.id.editButton).setOnClickListener {
                editOffer(offer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.added_offer_element,
            parent,
            false
        )

        return ViewHolder(inflatedView, offer, deleteOffer, editOffer)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(addedOfferList[position])
        holder.clickOnOffer(addedOfferList[position])
    }

    override fun getItemCount(): Int {
        return addedOfferList.size
    }
}