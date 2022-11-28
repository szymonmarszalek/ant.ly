package com.example.antly

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.databinding.FragmentUserAccountBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserAccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            logoutButton.setOnClickListener {
                viewModel.logout()
                val intent = Intent(activity, RegistrationActivity::class.java)
                startActivity(intent)
            }

            addNewOfferButton.setOnClickListener {
                navigateToChosenFragment(view,R.id.useNewOffer)
            }

            userFavoritesOfferButton.setOnClickListener {
                navigateToChosenFragment(view,R.id.useBoughtCourses)
            }

            userAddedOffersButton.setOnClickListener {
                navigateToChosenFragment(view,R.id.useAddedCourses)
            }

            usernameTextView.text = viewModel.getLoggedUserName()
        }
    }

    private fun navigateToChosenFragment(view: View, fragment: Int) {
        view.findNavController().popBackStack()
        view.findNavController().navigate(fragment)
    }
}