package com.example.antly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antly.data.dto.Level

class LevelAdapter(private val level: (String) -> Unit) :
    RecyclerView.Adapter<LevelAdapter.ViewHolder>() {

    private val subjectCategories = mutableListOf<Level>()

    @SuppressLint("NotifyDataSetChanged")
    fun setLevels(categories: List<Level>) {
        if (subjectCategories.isNotEmpty())
            subjectCategories.clear()

        subjectCategories.addAll(categories)
        notifyDataSetChanged()
    }

    class ViewHolder(
        val view: View,
        val subject: (String) -> Unit,
    ) : RecyclerView.ViewHolder(view) {
        fun bind(level: Level) {
            view.findViewById<TextView>(R.id.subject_category).text = level.levelName
            view.findViewById<ConstraintLayout>(R.id.subject_icon).background =
                ContextCompat.getDrawable(view.context, level.iconName);

            view.setOnClickListener {
                subject(level.levelName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.subject_list_element,
            parent,
            false
        )

        return ViewHolder(inflatedView, level)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subjectCategories[position])
    }

    override fun getItemCount(): Int {
        return subjectCategories.size
    }
}