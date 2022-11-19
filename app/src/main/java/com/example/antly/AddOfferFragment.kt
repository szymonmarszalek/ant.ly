package com.example.antly

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.databinding.FragmentAddOfferBinding

import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class AddOfferFragment : Fragment() {

    private val viewModel: AddNewOfferViewModel by viewModels()
    private val sharedViewModel: AddOfferSharedViewModel by activityViewModels()
    private var _binding: FragmentAddOfferBinding? = null
    private val binding get() = _binding!!
    private var offerTitle: String? = null
    private var offerSubject: String? = null
    private var offerLevel: String? = null
    private var offerImageUrl: String? = null
    private var offerShortDescription: String? = null
    private var offerLongDescription: String? = null
    private var offerLocation: String? = null
    private var offerPrice: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerTitle = sharedViewModel.offerTitle.value
        offerSubject = sharedViewModel.offerSubject.value
        offerLevel = sharedViewModel.offerLevel.value
        offerImageUrl = sharedViewModel.offerImageUrl.value
        offerShortDescription = sharedViewModel.offerShortDescription.value
        offerLongDescription = sharedViewModel.offerLongDescription.value
        offerLocation = sharedViewModel.offerLocation.value
        offerPrice = sharedViewModel.offerPrice.value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.autofill_list, R.id.text_view_list_item, getCountryCode(requireContext()))


        binding.apply {
            setSubjectAndLevelWhenNotNull(offerSubject, offerLevel)

            setFieldsIfTheyAreNotEmpty(offerTitle,
                offerImageUrl,
                offerShortDescription,
                offerLongDescription,
                offerLocation,
                offerPrice
            )

            localizationInput.setAdapter(adapter)
            categoryContainer.setOnClickListener {
                val bundle = bundleOf("type_of_choosing_subject" to "add_offer")
                requireActivity()
                    .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                    .findNavController()
                    .navigate(R.id.action_useNewOffer_to_subjectsFragment, bundle)
            }

            levelContainer.setOnClickListener {
                val bundle = bundleOf("type_of_choosing_level" to "add_offer")
                requireActivity()
                    .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                    .findNavController()
                    .navigate(R.id.action_useNewOffer_to_levelFragment, bundle)
            }

            addOffer.setOnClickListener {
                if (
                    offerTitle?.isNotEmpty() == true &&
                    offerSubject?.isNotEmpty() == true &&
                    offerLevel?.isNotEmpty() == true &&
                    offerImageUrl?.isNotEmpty() == true &&
                    offerLocation?.isNotEmpty() == true &&
                    offerShortDescription?.isNotEmpty() == true  &&
                    offerLongDescription?.isNotEmpty() == true &&
                    offerPrice != null
                ) {

                    viewModel.addOffer(
                        Offer(
                            offerTitle!!,
                            offerShortDescription!!,
                            offerLongDescription!!,
                            offerSubject!!,
                            offerLocation!!,
                            offerImageUrl!!,
                            offerPrice!!,
                            offerLevel!!,
                        )
                    )
                } else {
                    checkWhichFieldsAreEmptyAndShowMessage()
                }
            }
        }


        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    requireActivity()
                        .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                        .findNavController()
                        .navigate(R.id.action_useNewOffer_to_useHome)

                    cleanFields()
                }
                is Resource.Error -> TODO()
                is Resource.Loading -> {
                    binding.AddOfferScrollView.visibility = View.GONE
                    binding.progressBarCyclic.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun FragmentAddOfferBinding.checkWhichFieldsAreEmptyAndShowMessage() {
        showInformationAboutEmptyField(requiredTitle, offerTitle)
        showInformationAboutEmptyField(requiredShortDescription, offerShortDescription)
        showInformationAboutEmptyField(requiredLongDescription, offerLongDescription)
        showInformationAboutEmptyField(requiredSubject, offerSubject)
        showInformationAboutEmptyField(requiredLocation, offerLocation)
        showInformationAboutEmptyField(requiredPictureUrl, offerImageUrl)
        showInformationAboutEmptyField(requiredPrice, offerPrice)
        showInformationAboutEmptyField(requiredLevel, offerLevel)
    }

    private fun showInformationAboutEmptyField(requireFieldTextView: TextView, value: String?) {
        if (value.isNullOrEmpty()) {
            requireFieldTextView.visibility = View.VISIBLE
        } else {
            requireFieldTextView.visibility = View.INVISIBLE
        }
    }

    private fun showInformationAboutEmptyField(requireFieldTextView: TextView, value: Int?) {
        if (value == null) {
            requireFieldTextView.visibility = View.VISIBLE
        } else {
            requireFieldTextView.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentAddOfferBinding.setFieldsIfTheyAreNotEmpty(
        title: String?,
        imageUrl: String?,
        shortDescription: String?,
        longDescription: String?,
        location: String?,
        price: Int?,
    ) {


        subjectInputTextField.apply {
            setText(title)

            doOnTextChanged { text, _, _, _ ->
                offerTitle = text.toString()
                maxOfSignsTitle.text = "${text?.length}/30"
                sharedViewModel.offerTitle.value = text.toString()
            }
        }

        localizationInput.apply {
            setText(location)

            doOnTextChanged { text, _, _, _ ->
                offerLocation = text.toString()
                sharedViewModel.offerLocation.value = text.toString()
            }
        }

        offerShortDescriptionTextField.apply {
            setText(shortDescription)

            doOnTextChanged { text, _, _, count ->
                maxOfSignsShort.text = "${text?.length}/70"
                offerShortDescription = text.toString()
                sharedViewModel.offerShortDescription.value = text.toString()
            }
        }

        offerLongDescriptionTextField.apply {
            setText(longDescription)

            doOnTextChanged { text, _, _, _ ->
                maxOfSignsLong.text = "${text?.length}/200"
                offerLongDescription = text.toString()
                sharedViewModel.offerLongDescription.value = text.toString()
            }
        }

        pictureUrlTextField.apply {
            setText(imageUrl)

            doOnTextChanged { text, _, _, _ ->
                offerImageUrl = text.toString()
                sharedViewModel.offerImageUrl.value = text.toString()
            }
        }

        offerPriceTextField.apply {
            if (offerPrice != null)
                setText(price.toString())

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.isNotBlank()) {
                        offerPrice = text.toString().toInt()
                        sharedViewModel.offerPrice.value = text.toString().toInt()
                    }
                }
            }
        }
    }

    private fun FragmentAddOfferBinding.setSubjectAndLevelWhenNotNull(
        offerSubject: String?,
        offerLevel: String?,
    ) {
        if (offerSubject != null) {
            categoryTextView.text = offerSubject
            categoryTextView.setTextColor(resources.getColor(R.color.black))
        }

        if (offerLevel != null) {
            levelTextView.text = offerLevel
            levelTextView.setTextColor(resources.getColor(R.color.black))
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

    private fun cleanFields() {
        offerTitle = null
        offerSubject = null
        offerLevel = null
        offerImageUrl = null
        offerShortDescription = null
        offerLongDescription = null
        offerLocation = null
        offerPrice = null
        sharedViewModel.cleanFields()
    }
}