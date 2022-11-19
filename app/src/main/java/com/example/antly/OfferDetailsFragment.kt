package com.example.antly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentOfferDetailsBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class OfferDetailsFragment : Fragment() {

    private var _binding: FragmentOfferDetailsBinding? = null
    private val binding get() = _binding!!
    private var offer: OfferResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        offer = arguments?.getParcelable("offer")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOfferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

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
            offerLongDescriptionValueTextView.text = offer!!.descriptionLong
            offerShortDescriptionValueTextView.text = offer!!.descriptionShort

            offerDetailsTagsRecycleView.apply {
               layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
                adapter = OfferDetailsTagsAdapter(listOf(offer!!.location,offer!!.range,offer!!.subject))
            }
        }
    }
}