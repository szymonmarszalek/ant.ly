package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentUserFavoriteOffersBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserFavoriteOffersFragment : Fragment() {
    private var _binding: FragmentUserFavoriteOffersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserFavoriteOffersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserFavoriteOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AllOfferAdapter({ showOfferDetails(it) }, { }, {deleteFromFavorites(it)})
        binding.apply {

            addedOffersRecycleView.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                this.adapter = adapter
            }

            viewModel.getFavoriteOffers()

            viewModel.getFavorites.observe(viewLifecycleOwner) { favouriteOffers ->
                when (favouriteOffers) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        var offers = mutableListOf<OfferResponse>()

                        favouriteOffers.data!!.map {
                            offers.add(OfferResponse(
                                it.id,
                                it.title,
                                it.descriptionShort,
                                it.descriptionLong,
                                it.subject,
                                it.location,
                                it.imageUrl,
                                it.price,
                                it.teacherName,
                                it.range
                            ))
                        }
                        val offerIdList: List<Int> = favouriteOffers.data.map {
                            it.id
                        }
                        adapter.setFavoritesList(offerIdList)

                        adapter.setOfferList(offers)
                    }
                }
            }

            viewModel.deleteFavorite.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        viewModel.getFavoriteOffers()
                    }
                }
            }

        }
    }

    private fun deleteFromFavorites(offer: OfferResponse) {
        viewModel.deleteOfferFromFavorites(offer)
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
        bundle.putString("offer_details", "user_fav_offer")
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_fav_offers)
            .findNavController()
            .navigate(R.id.action_userFavoriteOffersFragment_to_offerDetailsFragment, bundle)
    }
}