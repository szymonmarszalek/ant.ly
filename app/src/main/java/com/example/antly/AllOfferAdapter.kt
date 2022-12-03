package com.example.antly

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.OfferResponse
import com.squareup.picasso.Picasso

class AllOfferAdapter(
    private val offer: (OfferResponse) -> Unit,
    private val addToFavorites: (OfferResponse) -> Unit,
    private val deleteFromFavorites: (OfferResponse) -> Unit,
) :
    RecyclerView.Adapter<AllOfferAdapter.ViewHolder>() {

    private var allOffersList = listOf<OfferResponse>()
    var favoritesList = mutableListOf<Int>()

    @SuppressLint("NotifyDataSetChanged")
    fun setOfferList(allOffers: List<OfferResponse>) {

        allOffersList = allOffers
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setFavoritesList1")
    fun setFavoritesList(allOffers: List<Int>) {
        favoritesList = allOffers as MutableList<Int>

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setFavoritesList1")
    fun addToFavorites(id: Int) {
        favoritesList.add(id)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setFavoritesList2")
    fun deleteFromFavorites(id: Int) {
        favoritesList.remove(id)

        notifyDataSetChanged()
    }

    class ViewHolder(
        val view: View,
        private val offer: (OfferResponse) -> Unit,
        private val addToFavorites: (OfferResponse) -> Unit,
        private val deleteFromFavorites: (OfferResponse) -> Unit,
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
                .into(view.findViewById<ImageButton>(R.id.offerImage))
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun setHeart() {
            view.findViewById<ImageView>(R.id.heartIcon).setImageDrawable(view.context.getDrawable(R.drawable.cards_heart))
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun clickOnOffer(offer: OfferResponse, favoritesList: List<Int>) {
            view.setOnClickListener {
                offer(offer)
            }
            view.findViewById<ImageView>(R.id.heartIcon).setOnClickListener {
                if(offer.id !in favoritesList) {
                    addToFavorites(offer)
                } else {
                    deleteFromFavorites(offer)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.offer_element,
            parent,
            false
        )

        return ViewHolder(inflatedView, offer, addToFavorites, deleteFromFavorites)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(allOffersList[position].id in favoritesList) {
            holder.setHeart()
        }

        holder.bind(allOffersList[position])
        holder.clickOnOffer(allOffersList[position],favoritesList)
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return allOffersList.size
    }
}