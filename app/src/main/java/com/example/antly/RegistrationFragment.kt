package com.example.antly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.data.RegisterData
import com.example.antly.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.HTTP

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerButton.setOnClickListener {
                nameAndAllErrorTextView.visibility = View.GONE
                passwordErrorTextView.visibility = View.GONE
                if (
                    loginInput.text.isNullOrEmpty() ||
                    passwordInput.text.isNullOrEmpty() ||
                    repeatPasswordInput.text.isNullOrEmpty() ||
                    nameInput.text.isNullOrEmpty() ||
                    surnameInput.text.isNullOrEmpty()
                ) {
                    showError(nameAndAllErrorTextView, R.string.fill_all)
                } else if (passwordInput.text!!.toString() != repeatPasswordInput.text.toString()) {
                    println(passwordInput.text)
                    println(repeatPasswordInput.text)
                    showError(passwordErrorTextView, R.string.passwords_are_not_the_same)
                } else if (loginInput.text!!.length < 4) {
                    showError(nameAndAllErrorTextView, R.string.login_too_short)
                } else if (passwordInput.text!!.length < 4) {
                    showError(nameAndAllErrorTextView, R.string.password_too_short)
                } else {
                    viewModel.register(
                        RegisterData(
                            binding.loginInput.text.toString(),
                            binding.passwordInput.text.toString(),
                            binding.nameInput.text.toString(),
                            binding.surnameInput.text.toString()
                        )
                    )
                }
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Error -> {
                    Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT)
                        .show()
                    binding.nameAndAllErrorTextView.visibility = View.VISIBLE
                    binding.progressBarCyclic.visibility = View.GONE
                    binding.registrationInput.visibility = View.VISIBLE
                    println(it.message)
                    if(it.message == "409") {
                        binding.nameAndAllErrorTextView.text = getString(R.string.username_taken)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarCyclic.visibility = View.VISIBLE
                    binding.registrationInput.visibility = View.GONE
                }
                is Resource.Success -> {
                    view.findNavController().popBackStack()
                }
            }
        }
    }

    private fun showError(textView: TextView, errorValue: Int) {
        textView.visibility = View.VISIBLE
        textView.text = getString(errorValue)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}