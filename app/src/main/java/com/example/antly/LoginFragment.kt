package com.example.antly


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.databinding.FragmentLoginBinding
import com.example.antly.databinding.FragmentSecondBinding
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
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val createAccountTextView = view.findViewById<TextView>(R.id.createAccountTextView)
        val loginInput = view.findViewById<EditText>(R.id.loginInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)

        loginButton.setOnClickListener {
            loginViewModel.login(binding.loginInput.text.toString(), binding.passwordInput.text.toString())

            loginViewModel.viewState.observe(viewLifecycleOwner) {
                when(it) {
                    is Resource.Success -> loginUser()
                    is Resource.Loading -> println("ładowanie")
                    is Resource.Error -> println(it.message)
                }
            }
        }

        createAccountTextView.setOnClickListener {
            requireActivity()
                .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                .findNavController()
                .navigate(R.id.action_loginFragment_to_secondFragment)
        }
    }

    private fun loginUser() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}