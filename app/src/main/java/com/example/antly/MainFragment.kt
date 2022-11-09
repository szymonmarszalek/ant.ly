package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentMainBinding
import com.example.antly.databinding.FragmentSecondBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {
    private val viewModel: MainViewModel by viewModels()
     var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllOffers()
        var offersAdapter: AllOfferAdapter? = null
        viewModel.viewState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success ->  {
                    offersAdapter = it.data?.let { it1 -> AllOfferAdapter(it1) }
                    binding.recycleView.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = offersAdapter
                    }
                }
            }
        }

        val addNewOfferButton = view.findViewById<Button>(R.id.addNewOfferButton)

        addNewOfferButton.setOnClickListener {
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                .findNavController()
                .navigate(R.id.action_mainFragment_to_addOfferFragment)
        }

        binding.searchEditText.setOnClickListener {
                binding.addDetailsContainer.visibility = View.VISIBLE
                binding.addDetailsContainer.startAnimation( AnimationUtils.loadAnimation(context,R.anim.slide_from_bottom))
        }

        binding.root.setOnClickListener {
            binding.addDetailsContainer.startAnimation( AnimationUtils.loadAnimation(context,R.anim.hide))
            binding.addDetailsContainer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}