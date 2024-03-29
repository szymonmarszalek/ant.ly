package com.example.antly


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

//
//
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val loginViewModel: LoginViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            if (binding.loginInput.text.isNullOrEmpty() || binding.passwordInput.text.isNullOrEmpty()) {
                binding.errorTextView.visibility = View.VISIBLE
            } else {
                loginViewModel.login(binding.loginInput.text.toString(),
                    binding.passwordInput.text.toString())
            }

        }

        loginViewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> loginUser()
                is Resource.Loading -> {
                    binding.progressBarCyclic.visibility = View.VISIBLE
                    binding.loginContainer.visibility = View.GONE
                }
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.progressBarCyclic.visibility = View.GONE
                    binding.loginContainer.visibility = View.VISIBLE
                }
            }
        }

        binding.signUpTextView.setOnClickListener {
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_registration)
                .findNavController()
                .navigate(R.id.action_loginFragment_to_secondFragment)
        }
    }

    private fun loginUser() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
        }

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}