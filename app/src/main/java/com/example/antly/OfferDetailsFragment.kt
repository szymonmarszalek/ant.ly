package com.example.antly

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.data.dto.FavoriteOffer
import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentOfferDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfferDetailsFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val userDetailsViewModel: UserDetailsViewModel by viewModels()

    private var type: String? = null
    private var _binding: FragmentOfferDetailsBinding? = null
    private val binding get() = _binding!!
    private var offer: OfferResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        offer = arguments?.getParcelable("offer")
        type = arguments?.getString("offer_details")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOfferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDetailsViewModel.getFavoriteOffers()
        var favoriteOffers = listOf<FavouritesDto>()

        userDetailsViewModel.getFavorites.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    favoriteOffers = it.data!!


                    if(it.data.filter { it.id == offer!!.id }.size == 1) {
                        binding.heartIcon.setImageDrawable(view.context.getDrawable(R.drawable.cards_heart))
                    } else {
                       // binding.heartIcon.setImageDrawable(view.context.getDrawable(R.drawable.cards_heart_outline))
                    }
                }
            }
            userDetailsViewModel.getFavorites.postValue(null)
        }
        binding.apply {
            Picasso
                .get()
                .load(offer?.imageUrl)
                .error(R.drawable.student_icon)
                .noPlaceholder()
                .into(offerImageView)

            offerDetailsTitleTextView.text = offer!!.title
            offerDetailsPriceTextView.text = offer!!.price.toString() + " £ /h"
            offerDetailsTeacherTextView.text = offer!!.teacherName
            offerShortDescriptionValueTextView.text = offer!!.descriptionShort

            offerDetailsTagsRecycleView.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
                adapter = OfferDetailsTagsAdapter(listOf(offer!!.location,
                    offer!!.range,
                    offer!!.subject))
            }

            heartIcon.setOnClickListener {
                if(favoriteOffers.none { it.id == offer!!.id }) {
                    userDetailsViewModel.addOfferToFavorites(offer!!.id)
                } else {
                    userDetailsViewModel.deleteOfferFromFavorites(offer!!)
                }
            }

            userDetailsViewModel.deleteFavorite.observe(viewLifecycleOwner) {
                when(it){
                    is Resource.Error ->    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        userDetailsViewModel.getFavoriteOffers()
                        heartIcon.setImageDrawable(view.context.getDrawable(R.drawable.cards_heart_outline))
                    }
                }
            }

            userDetailsViewModel.addToFavorites.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        userDetailsViewModel.getFavoriteOffers()
                        heartIcon.setImageDrawable(view.context.getDrawable(R.drawable.cards_heart))
                    }
                }
                userDetailsViewModel.addToFavorites.postValue(null)
            }

            showLongDescriptionContainer.setOnClickListener { view ->
                view.visibility = View.GONE

                offerDetailsLongDescriptionValueContainer.visibility = View.VISIBLE
                offerLongDescriptionValueTextView.text = offer!!.descriptionLong
            }

            backButton.setOnClickListener {
                if (type == "user_offer") {
                    goBackUserOffers(view)
                } else if(type == "user_fav_offer") {
                    goBackFavoriteOffers(view)
                } else {
                    goBackAllOffers(view)
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback {
                if (type == "user_offer") {
                    goBackUserOffers(view)
                } else if(type == "user_fav_offer") {
                   goBackFavoriteOffers(view)
                } else {
                    goBackAllOffers(view)
                }
            }

            if (type == "user_offer") {
                userEditContainer.visibility = View.VISIBLE

                deleteOfferButtonContainer.setOnClickListener {
                    deleteOffer(offer!!.id)
                }

                editButtonContainer.setOnClickListener {
                    goToEditOfferPage(offer!!.id)
                }

                userDetailsViewModel.deleteOfferState.observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Error -> {
                            Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> goBackUserOffers(view)
                    }

                    userDetailsViewModel.deleteOfferState.postValue(null)
                }
            }
        }
    }

    private fun goToEditOfferPage(offerId: Int) {
        val bundle: Bundle = bundleOf("offer_id" to offerId)

        sharedViewModel.isPopBackStack = true
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_user_added_offers)
            .findNavController()
            .navigate(R.id.action_offerDetailsFragment_to_editOfferFragment, bundle)

    }

    private fun deleteOffer(offerId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.delete_offer_confirmation))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Yes)) { dialog, id ->
                userDetailsViewModel.deleteOffer(offerId)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun goBackAllOffers(view: View) {
        sharedViewModel.isPopBackStack = true
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_all_offers)
            .findNavController()
            .popBackStack()

    }

    private fun goBackFavoriteOffers(view: View) {
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_fav_offers)
            .findNavController()
            .popBackStack()

    }

    private fun goBackUserOffers(view: View) {
        sharedViewModel.isPopBackStack = true
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_user_added_offers)
            .findNavController()
            .popBackStack()
    }
}