package com.example.antly


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var subject: String? = null
    private var level: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chosenSubject = arguments?.getString("subject")
        val level = arguments?.getString("level")
        subject = chosenSubject
        this.level = level
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllOffers()
        var layoutManagerOffer: LinearLayoutManager? =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        var offersAdapter: AllOfferAdapter?

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

        binding.subjectSpinnerContainer.setOnClickListener {
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                .findNavController()
                .navigate(R.id.action_useHome_to_subjectsFragment)
        }


        binding.levelContainer.setOnClickListener {
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                .findNavController()
                .navigate(R.id.action_useHome_to_levelFragment)
        }

        if (subject != null) {
            binding.searchBarConstraintLayout.visibility = View.GONE
            binding.addDetailsContainer.visibility = View.VISIBLE
            binding.subjectCategoryTextview.text = subject.toString()
        }

        getCountryCode(requireContext())
        if (level != null) {
            binding.searchBarConstraintLayout.visibility = View.GONE
            binding.addDetailsContainer.visibility = View.VISIBLE
            binding.levelTextView.text = level.toString()
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.autofill_list, R.id.text_view_list_item, getCountryCode(requireContext()))

        binding.localizationInput.setAdapter(adapter)

        binding.searchEditText.setOnClickListener {
            binding.searchBarContainer.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.sliding))
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