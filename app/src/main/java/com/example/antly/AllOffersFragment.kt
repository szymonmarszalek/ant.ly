package com.example.antly

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.data.dto.FavouritesDto
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentAllOffersBinding
import com.example.antly.helpers.SoftKeyboard
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class AllOffersFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentAllOffersBinding? = null
    private val binding get() = _binding!!
    private var subject: String? = null
    private var level: String? = null
    private var favIdOffer: Int? = null
    private var location: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //typeOfChoosing = arguments?.getString("subject")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var offers: List<OfferResponse> = sharedViewModel.offerList
        var favoriteOffers: List<FavouritesDto> = listOf()
        val layoutManagerOffer: LinearLayoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        val offersAdapter =
            AllOfferAdapter({ it -> showOfferDetails(it) }, { addOfferToFavorites(it.id) }, {deleteFromFavorites(it)})




        level = sharedViewModel.range.value
        subject = sharedViewModel.subject.value
        location = sharedViewModel.localization.value

        binding.localizationInput.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                //SoftKeyboard.hide(requireActivity())
                sharedViewModel.localization.value = binding.localizationInput.text.toString()
                location = binding.localizationInput.text.toString()
                searchForOffers()
            }

        viewModel.viewAllOfferState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    sharedViewModel.offerList = it.data!!
                    binding.recycleView.apply {
                        binding.progressBarCyclic.visibility = View.GONE
                        layoutManager = layoutManagerOffer
                        adapter = offersAdapter
                    }
                    getFavoriteOffers()
                    offers = it.data
                    if(it.data.isEmpty()) {
                        binding.noResultsInformation.visibility = View.VISIBLE
                    } else {
                        binding.noResultsInformation.visibility = View.GONE
                    }

                }
                is Resource.Loading -> binding.progressBarCyclic.visibility = View.VISIBLE
                is Resource.Error -> {
                    binding.progressBarCyclic.visibility = View.GONE
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.getFavorites.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    favoriteOffers = it.data!!
                    offersAdapter.setOfferList(offers)
                    val offerIdList: List<Int> = it.data.map {
                        it.id
                    }
                    offersAdapter.setFavoritesList(offerIdList)
                }
            }

        }
        binding.subjectSpinnerContainer.setOnClickListener {
            navigateToChoosingSubjectFragment()
        }


        binding.levelContainer.setOnClickListener {
            navigateToChoosingRangeFragment()
        }
        binding.chosenSubject.setOnClickListener {
            navigateToChoosingSubjectFragment()
        }
        binding.chosenLocationContainer.setOnClickListener {
            showSearchDetails()
            binding.localizationInput.requestFocus()
            SoftKeyboard.show(requireActivity(),binding.localizationInput)
        }

        if (sharedViewModel.isSearchBarOpened) {
            binding.searchBarConstraintLayout.visibility = View.GONE
            binding.addDetailsContainer.visibility = View.VISIBLE

            if (subject != null)
                binding.subjectCategoryTextview.text = subject
            if (level != null)
                binding.levelTextView.text = level.toString()
            if (location != null)
                binding.localizationInput.setText(location)
        }



        getCountryCode(requireContext())

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.autofill_list, R.id.text_view_list_item, getCountryCode(requireContext()))

        binding.localizationInput.setAdapter(adapter)
        binding.cancelSearchButton.setOnClickListener {
            cleanValues()
            cleanFields()
            binding.ideas.visibility = View.GONE
            searchForOffers()

        }
        if (sharedViewModel.isPopBackStack) {
            sharedViewModel.isPopBackStack = false
            offers = sharedViewModel.offerList

            if(subject != null && level != null && location != null) {
                searchForOffers()
            } else {
                offersAdapter.setOfferList(sharedViewModel.offerList)
            }
        } else {
            searchForOffers()
        }

        binding.searchEditText.setOnClickListener {
            if (subject != null)
                binding.subjectCategoryTextview.text = subject
            if (level != null)
                binding.levelTextView.text = level.toString()
            if (location != null)
                binding.localizationInput.setText(location)


            showSearchDetails()
        }

        binding.chosenRange.setOnClickListener {
            navigateToChoosingRangeFragment()
        }

        binding.hideButtonContainer.setOnClickListener {
            sharedViewModel.isSearchBarOpened = false
            binding.searchBarConstraintLayout.visibility = View.VISIBLE
            binding.addDetailsContainer.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.hide
                )
            )
            binding.addDetailsContainer.visibility = View.GONE
        }

        requireActivity().onBackPressedDispatcher.addCallback {

        }

        viewModel.addToFavorites.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    offersAdapter.addToFavorites(favIdOffer!!)
                    getFavoriteOffers()
                }
            }
            viewModel.addToFavorites.postValue(null)
        }

        viewModel.deleteFavorite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    offersAdapter.deleteFromFavorites(favIdOffer!!)
                    getFavoriteOffers()
                }
            }
            viewModel.deleteFavorite.postValue(null)
        }


    }

    private fun deleteFromFavorites(offer: OfferResponse) {
        viewModel.deleteOfferFromFavorites(offer)
        favIdOffer = offer.id
    }

    private fun showSearchDetails() {
        sharedViewModel.isSearchBarOpened = true
        binding.searchBarContainer.startAnimation(
            AnimationUtils.loadAnimation(context,
                R.anim.sliding
            )
        )

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

    private fun navigateToChoosingSubjectFragment() {
        val bundle = bundleOf("type_of_choosing_subject" to "find_offer")
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_all_offers)
            .findNavController()
            .navigate(R.id.action_fragment_all_offers_to_subjectsFragment, bundle)
    }

    private fun navigateToChoosingRangeFragment() {
        val bundle = bundleOf("type_of_choosing_level" to "find_offer")
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_all_offers)
            .findNavController()
            .navigate(R.id.action_fragment_all_offers_to_levelFragment, bundle)
    }

    private fun getFavoriteOffers() {
        viewModel.getFavoriteOffers()
    }

    private fun showOfferDetails(offerId: OfferResponse) {
        val bundle = Bundle()
        bundle.putParcelable("offer", offerId)
        requireActivity()
            .findViewById<FragmentContainerView>(R.id.nav_host_all_offers)
            .findNavController()
            .navigate(R.id.action_fragment_all_offers_to_offerDetailsFragment, bundle)
    }

    private fun cleanValues() {
        subject = null
        level = null
        location = null
        sharedViewModel.cleanValues()
    }

    private fun cleanFields() {
        binding.localizationInput.text.clear()
        binding.subjectCategoryTextview.text = getString(R.string.subject_category)
        binding.levelTextView.text = getString(R.string.level)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addOfferToFavorites(offerId: Int) {
        favIdOffer = offerId
        viewModel.addOfferToFavorites(offerId)
    }

    private fun searchForOffers() {
        if ((subject != null) && (level != null) && (location != null)) {
            viewModel.getFilteredOffers(level!!, subject!!, location!!)
            SoftKeyboard.hide(requireActivity())
            binding.searchBarConstraintLayout.visibility = View.VISIBLE

            binding.addDetailsContainer.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.hide
                )
            )
            binding.addDetailsContainer.visibility = View.GONE
            sharedViewModel.isSearchBarOpened = false
            //cleanValues()

            binding.ideas.visibility = View.VISIBLE
            binding.searchRange.text = level
            binding.searchLocation.text = location
            binding.searchSubject.text = subject
        } else {
            viewModel.getAllOffers()
        }
    }

    private fun getCountryCode(context: Context): List<String> {
        val cities = mutableListOf<String>()
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("uk_cities.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {

        }
        val jsonObject = JSONObject(jsonString)

        try {
            val jsonObj = jsonObject
            val arrayJson: JSONArray = jsonObj.getJSONArray("United Kingdom")

            for (i in 0 until arrayJson.length()) {
                val error = arrayJson.getString(i)
                cities.add(error)
            }


        } catch (ex: JSONException) {
            ex.printStackTrace()
        }

        return cities
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}