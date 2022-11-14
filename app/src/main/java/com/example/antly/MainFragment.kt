package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllOffers()
        var layoutManagerOffer: LinearLayoutManager? =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        var offersAdapter: AllOfferAdapter? = null
        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarCyclic.visibility = View.GONE
                    offersAdapter = it.data?.let { it1 -> AllOfferAdapter(it1) }
                    binding.recycleView.apply {
                        layoutManager = layoutManagerOffer
                        adapter = offersAdapter
                    }
                }
                is Resource.Loading -> binding.progressBarCyclic.visibility = View.VISIBLE
                is Resource.Error -> binding.progressBarCyclic.visibility = View.GONE
            }
        }

        binding.searchEditText.setOnClickListener {
            binding.searchBarContainer.startAnimation(AnimationUtils.loadAnimation(context,R.anim.sliding))
            binding.searchBarConstraintLayout.visibility = View.GONE
            binding.addDetailsContainer.visibility = View.VISIBLE
            binding.searchBarConstraintLayout.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.hide
                )
            )
            binding.addDetailsContainer.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.slide_from_bottom
                )
            )
        }

        binding.root.setOnClickListener {
            binding.searchBarConstraintLayout.visibility = View.VISIBLE
            binding.addDetailsContainer.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.hide
                )
            )
            binding.addDetailsContainer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}