package com.example.antly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.antly.data.RegisterData
import com.example.antly.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginInput = view.findViewById<EditText>(R.id.loginInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val nameInput = view.findViewById<EditText>(R.id.name)
        val surnameInput = view.findViewById<EditText>(R.id.surname)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            viewModel.register(
                RegisterData(
                    binding.loginInput.text.toString(),
                    binding.passwordInput.text.toString(),
                    binding.name.text.toString(),
                    binding.surname.text.toString()
                )
            )
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            println(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}