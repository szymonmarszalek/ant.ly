package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentUserAddedOffersBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserAddedOffersFragment : Fragment() {
    private var _binding: FragmentUserAddedOffersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserAddedOffersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserAddedOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AddedOfferAdapter({ it -> println(it) }, { deleteOffer(it.id) })
        binding.apply {

            addedOffersRecycleView.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                this.adapter = adapter
            }

            viewModel.getAddedOffers()

            viewModel.addedOffersState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> println("error")
                    is Resource.Loading -> showProgressBar()
                    is Resource.Success -> {
                        hideProgressBar()
                        if(it.data!!.isEmpty()) {
                            showNoOfferLayout()
                        } else {
                            adapter.setAddedOfferList(it.data)
                        }
                    }
                }
            }
        }

        viewModel.deleteOfferState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> println("NIE UDANO")
                is Resource.Loading -> {}
                is Resource.Success -> viewModel.getAddedOffers()
            }
        }
    }

    private fun showProgressBar() {
        binding.addedOffersRecycleView.visibility = View.GONE
        binding.progressBarCyclic.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBarCyclic.visibility = View.GONE
        binding.addedOffersRecycleView.visibility = View.VISIBLE
    }

    private fun showNoOfferLayout() {
        binding.noAddedOffersContainer.visibility = View.VISIBLE
        binding.addedOffersRecycleView.visibility = View.GONE
    }

    private fun deleteOffer(offerId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.delete_offer_confirmation))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Yes)) { dialog, id ->
                viewModel.deleteOffer(offerId)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}