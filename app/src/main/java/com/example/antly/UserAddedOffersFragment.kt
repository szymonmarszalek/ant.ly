package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentUserAddedOffersBinding
import com.google.android.material.snackbar.Snackbar
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
        _binding = FragmentUserAddedOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AddedOfferAdapter({ showOfferDetails(it) }, { deleteOffer(it.id) }, {editOffer(it.id)})
        binding.apply {

            addedOffersRecycleView.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                this.adapter = adapter
            }

            viewModel.getAddedOffers()

            viewModel.addedOffersState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
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
                viewModel.addedOffersState.postValue(null)
            }
        }

        viewModel.deleteOfferState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> viewModel.getAddedOffers()
            }
            viewModel.deleteOfferState.postValue(null)
        }
    }

    private fun editOffer(offerId: Int) {
        val bundle = Bundle()
        bundle.putInt("offer_id" , offerId)
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_fav_offers)
            .findNavController()
            .navigate(R.id.action_userAddedOffersFragment_to_editOfferFragment, bundle)
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

    private fun showOfferDetails(offerId: OfferResponse) {
        val bundle = Bundle()
        bundle.putParcelable("offer", offerId)
        bundle.putString("offer_details" , "user_offer")
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_user_added_offers)
            .findNavController()
            .navigate(R.id.action_userAddedOffersFragment_to_offerDetailsFragment, bundle)
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