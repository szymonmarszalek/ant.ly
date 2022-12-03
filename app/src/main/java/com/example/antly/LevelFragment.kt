package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.data.dto.Level
import com.example.antly.databinding.FragmentLevelBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LevelFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val sharedAddOfferViewModel: AddOfferSharedViewModel by activityViewModels()
    private var _binding: FragmentLevelBinding? = null
    private val binding get() = _binding!!
    private var bundle: Bundle? = null
    private var typeOfChoosing: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLevels()

        typeOfChoosing = arguments?.getString("type_of_choosing_level")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        navBar.visibility = View.GONE

        var layoutManagerSubjects: LinearLayoutManager? =
            LinearLayoutManager(requireContext())

        var adapter = LevelAdapter() { chooseCategory(it) }

        binding.levelsRecycleView.apply {
            layoutManager = layoutManagerSubjects
            this.adapter = adapter
        }

        binding.cancelButton.setOnClickListener {
            sharedViewModel.isPopBackStack = true
            view.findNavController().popBackStack()

            val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
            navBar.visibility = View.VISIBLE
        }

        adapter.setLevels(getLevels())
    }

    private fun chooseCategory(category: String) {
        if (typeOfChoosing == "find_offer") {
            sharedViewModel.range.value = category
            sharedViewModel.isPopBackStack = true
            view?.findNavController()?.popBackStack()

        } else {
            sharedAddOfferViewModel.offerLevel.value = category
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_add_offer)
                .findNavController()
                .popBackStack()
        }

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        navBar.visibility = View.VISIBLE

    }

    companion object {
        @JvmName("getLevels")
        fun getLevels(): List<Level> {

            val levels = mutableListOf<Level>()
            levels.add(
                Level("Primary School", R.drawable.teddy_bear)
            )
            levels.add(
                Level("Secondary School", R.drawable.chair_school)
            )
            levels.add(
                Level("High School", R.drawable.student_icon)
            )
            levels.add(
                Level("University", R.drawable.account_school_outline)
            )

            return levels
        }
    }
}