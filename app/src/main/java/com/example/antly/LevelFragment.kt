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
import com.example.antly.data.dto.Level
import com.example.antly.data.dto.SubjectCategory
import com.example.antly.databinding.FragmentLevelBinding
import com.example.antly.databinding.FragmentSubjectsBinding

class LevelFragment : Fragment() {

    private var _binding: FragmentLevelBinding? = null
    private val binding get() = _binding!!
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLevels()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var layoutManagerSubjects: LinearLayoutManager? =
            LinearLayoutManager(requireContext())

        var adapter = LevelAdapter() {chooseCategory(it)}

        binding.levelsRecycleView.apply {
            layoutManager = layoutManagerSubjects
            this.adapter = adapter
        }

        adapter.setLevels(getLevels())
    }

    private fun chooseCategory(category: String) {
        val bundle = bundleOf("level" to category)
        println(category)
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
            .findNavController()
            .navigate(R.id.action_levelFragment_to_useHome,bundle)
    }

    companion object {
        @JvmName("getLevels")
        fun getLevels(): List<Level> {

            val levels = mutableListOf<Level>()
            levels.add(
                Level("Primary School",R.drawable.teddy_bear)
            )
            levels.add(
                Level("Secondary School",R.drawable.chair_school)
            )
            levels.add(
                Level("High School",R.drawable.student_icon)
            )
            levels.add(
                Level("University",R.drawable.account_school_outline)
            )


            return levels
        }
    }
}