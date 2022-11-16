package com.example.antly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat.setBackground
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.OfferResponse
import com.example.antly.data.dto.SubjectCategory
import com.squareup.picasso.Picasso

class SubjectListAdapter(val subject: (String) -> Unit) :
RecyclerView.Adapter<SubjectListAdapter.ViewHolder>(){

    private val subjectCategories = mutableListOf<SubjectCategory>()
    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: List<SubjectCategory>) {
        if(subjectCategories.isNotEmpty())
            subjectCategories.clear()

        subjectCategories.addAll(categories)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View,
                     val subject: (String) -> Unit
    ): RecyclerView.ViewHolder(view) {
        fun bind(category: SubjectCategory) {
            view.findViewById<TextView>(R.id.subject_category).text = category.categoryName

            view.findViewById<ConstraintLayout>(R.id.subject_icon).background =
                ContextCompat.getDrawable(view.context, category.iconName);

            view.setOnClickListener {
                subject(category.categoryName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.subject_list_element,
            parent,
            false
        )

        return ViewHolder(inflatedView, subject)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subjectCategories[position])
    }

    override fun getItemCount(): Int {
        return subjectCategories.size
    }
}