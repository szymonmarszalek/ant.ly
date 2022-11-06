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
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_second,
            container,
            false
        )

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
                    loginInput.text.toString(),
                    passwordInput.text.toString(),
                    nameInput.text.toString(),
                    surnameInput.text.toString()
                )
            )
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            println(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}