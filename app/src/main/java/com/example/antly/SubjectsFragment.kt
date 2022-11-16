package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.data.dto.SubjectCategory
import com.example.antly.databinding.FragmentSubjectsBinding

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCategories()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var layoutManagerSubjects: LinearLayoutManager? =
            LinearLayoutManager(requireContext())

        var adapter = SubjectListAdapter() {chooseCategory(it)}

        binding.subjectsRecycleView.apply {
            layoutManager = layoutManagerSubjects
            this.adapter = adapter
        }

        adapter.setCategories(getCategories())
    }

    private fun chooseCategory(category: String) {
        val bundle = bundleOf("subject" to category)
            println(category)
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                .findNavController()
                .navigate(R.id.action_subjectsFragment_to_useHome,bundle)
    }

    companion object {
        @JvmName("getCategories1")
        fun getCategories(): List<SubjectCategory> {

            val categories = mutableListOf<SubjectCategory>()
            categories.add(
                SubjectCategory("Mathematics",R.drawable.calculator)
            )
            categories.add(
                SubjectCategory("English",R.drawable.chess_rook)
            )
            categories.add(
                SubjectCategory("French",R.drawable.eiffel_tower)
            )
            categories.add(
                SubjectCategory("Spanish",R.drawable.dance_ballroom)
            )
            categories.add(
                SubjectCategory("German",R.drawable.pretzel)
            )
            categories.add(
                SubjectCategory("Polish",R.drawable.chess_rook)
            )
            categories.add(
                SubjectCategory("Biology",R.drawable.dna)
            )
            categories.add(
                SubjectCategory("Chemistry",R.drawable.flask_empty_outline)
            )
            categories.add(
                SubjectCategory("History",R.drawable.flask_empty_outline)
            )
            categories.add(
                SubjectCategory("Early School Education",R.drawable.teddy_bear)
            )
            categories.add(
                SubjectCategory("Geography",R.drawable.map_legend)
            )
            categories.add(
                SubjectCategory("Physics",R.drawable.telescope)
            )
            categories.add(
                SubjectCategory("Information Technology",R.drawable.monitor)
            )
            categories.add(
                SubjectCategory("Music",R.drawable.music_note)
            )
            categories.add(
                SubjectCategory("Civics",R.drawable.account_group)
            )
            categories.add(
                SubjectCategory("Economy",R.drawable.chart_bar)
            )
            categories.add(
                SubjectCategory("Engineering Studies",R.drawable.hammer_screwdriver)
            )
            categories.add(
                SubjectCategory("Humanities Studies",R.drawable.account_group)
            )
            categories.add(
                SubjectCategory("Medical Studies",R.drawable.doctor)
            )
            categories.add(
                SubjectCategory("Language Studies",R.drawable.translate_variant)
            )

            return categories
        }
    }
}