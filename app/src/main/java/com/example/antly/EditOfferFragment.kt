package com.example.antly

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.data.dto.OfferResponse
import com.example.antly.databinding.FragmentEditOfferBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class EditOfferFragment : Fragment() {
    private val editOfferViewModel: EditOfferViewModel by viewModels()
    private var _binding: FragmentEditOfferBinding? = null
    private val binding get() = _binding!!
    private var offerTitle: String? = null
    private var offerSubject: String? = null
    private var offerLevel: String? = null
    private var offerImageUrl: String? = null
    private var offerShortDescription: String? = null
    private var offerLongDescription: String? = null
    private var offerLocation: String? = null
    private var offerPrice: Int? = null
    private var offer: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offer = arguments?.getInt("offer_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.autofill_list, R.id.text_view_list_item, getCountryCode(requireContext()))

        offer?.let { editOfferViewModel.getOfferById(it) }


        binding.apply {
            setSubjectAndLevelWhenNotNull(offerSubject, offerLevel)
            editOfferViewModel.viewStateOfferById.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {
                        progressBarCyclic.visibility = View.VISIBLE
                        addOfferContainer.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        progressBarCyclic.visibility = View.GONE
                        addOfferContainer.visibility = View.VISIBLE
                        setFieldsIfTheyAreNotEmpty(it.data!!)
                    }
                }
                editOfferViewModel.viewStateOfferById.postValue(null)
            }

            editOfferViewModel.viewStateEdit.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {
                        progressBarCyclic.visibility = View.VISIBLE
                        addOfferContainer.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        requireActivity()
                            .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                            .findNavController()
                            .navigate(R.id.useHome)
                    }
                }
                editOfferViewModel.viewStateEdit.postValue(null)
            }

            editOfferViewModel.viewStateEdit.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is Resource.Loading -> {
                        progressBarCyclic.visibility = View.VISIBLE
                        addOfferContainer.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        progressBarCyclic.visibility = View.GONE
                        addOfferContainer.visibility = View.VISIBLE
                    }
                }
            }
            editOfferViewModel.viewStateEdit.postValue(null)
            localizationInput.setAdapter(adapter)


            addOffer.setOnClickListener {
                if (
                    offerTitle?.isNotEmpty() == true &&
                    offerSubject?.isNotEmpty() == true &&
                    offerLevel?.isNotEmpty() == true &&
                    offerImageUrl?.isNotEmpty() == true &&
                    offerLocation?.isNotEmpty() == true &&
                    offerShortDescription?.isNotEmpty() == true &&
                    offerLongDescription?.isNotEmpty() == true &&
                    offerPrice != null
                ) {
                    editOfferViewModel.editOffer(offer!!, Offer(
                        offerTitle!!,
                        offerShortDescription!!,
                        offerLongDescription!!,
                        offerSubject!!,
                        offerLocation!!,
                        offerImageUrl!!,
                        offerPrice!!,
                        offerLevel!!
                    ))

                } else {
                    checkWhichFieldsAreEmptyAndShowMessage()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback {
               goBack()
            }
        }

    }

    private fun FragmentEditOfferBinding.checkWhichFieldsAreEmptyAndShowMessage() {
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


    private fun goBack() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.cancel_editing_offer))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Yes)) { dialog, id ->
                requireActivity()
                    .findViewById<FragmentContainerView>(R.id.nav_host_user_added_offers)
                    .findNavController()
                    .popBackStack()

                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.No)) { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentEditOfferBinding.setFieldsIfTheyAreNotEmpty(
        offer: OfferResponse,
    ) {

        offerTitle = offer.title
        offerSubject = offer.subject
        offerPrice = offer.price
        offerLongDescription = offer.descriptionLong
        offerShortDescription = offer.descriptionShort
        offerLevel = offer.range
        offerLocation = offer.location
        offerImageUrl = offer.imageUrl

        categoryTextView.text = offer.subject
        categoryTextView.setTextColor(resources.getColor(R.color.black))



        levelTextView.text = offer.range
        levelTextView.setTextColor(resources.getColor(R.color.black))


        subjectInputTextField.apply {
            setText(offer.title)

            doOnTextChanged { text, _, _, _ ->
                offerTitle = text.toString()
                maxOfSignsTitle.text = "${text?.length}/30"
            }
        }

        localizationInput.apply {
            setText(offer.location)

            doOnTextChanged { text, _, _, _ ->
                offerLocation = text.toString()
            }
        }

        offerShortDescriptionTextField.apply {
            setText(offer.descriptionShort)

            doOnTextChanged { text, _, _, count ->
                maxOfSignsShort.text = "${text?.length}/70"
                offerShortDescription = text.toString()
            }
        }

        offerLongDescriptionTextField.apply {
            setText(offer.descriptionLong)

            doOnTextChanged { text, _, _, _ ->
                maxOfSignsLong.text = "${text?.length}/200"
                offerLongDescription = text.toString()
            }
        }

        pictureUrlTextField.apply {
            setText(offer.imageUrl)

            doOnTextChanged { text, _, _, _ ->
                offerImageUrl = text.toString()
            }
        }

        offerPriceTextField.apply {
            setText(offer.price.toString())

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    if (text.isNotBlank()) {
                        offerPrice = text.toString().toInt()
                    }
                }
            }
        }
    }

    private fun FragmentEditOfferBinding.setSubjectAndLevelWhenNotNull(
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
    }
}