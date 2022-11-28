package com.example.antly

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentOfferDetailsBinding
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            showLongDescriptionContainer.setOnClickListener { view ->
                view.visibility = View.GONE

                offerDetailsLongDescriptionValueContainer.visibility = View.VISIBLE
                offerLongDescriptionValueTextView.text = offer!!.descriptionLong
            }

            backButton.setOnClickListener {
                goBack(view)
            }

            requireActivity().onBackPressedDispatcher.addCallback {
                goBack(view)
            }

            if (type == "user_offer") {
                userEditContainer.visibility = View.VISIBLE

                deleteOfferButtonContainer.setOnClickListener {
                    deleteOffer(offer!!.id)
                }
                userDetailsViewModel.deleteOfferState.observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Error -> {}
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> goBack(view)
                    }
                }
            }
        }
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

    private fun goBack(view: View) {
        sharedViewModel.isPopBackStack = true

        view
            .findNavController()
            .popBackStack()
    }
}