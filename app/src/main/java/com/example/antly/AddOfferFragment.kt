package com.example.antly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.antly.common.Resource
import com.example.antly.data.dto.Offer
import com.example.antly.databinding.FragmentAddOfferBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Observer

@AndroidEntryPoint
class AddOfferFragment : Fragment() {

    private val viewModel: AddNewOfferViewModel by viewModels()
    private var _binding: FragmentAddOfferBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOfferBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val price = view.findViewById<EditText>(R.id.priceInput)
        val addOfferButton = view.findViewById<Button>(R.id.addOffer)

        addOfferButton.setOnClickListener {
           var offer = Offer(subject = "Matematyka",
               description = binding.subjectDescriptionInput.text.toString(),
               price = price.text.toString().toInt(),
               title = binding.subjectInputTextField.text.toString()
           )


            viewModel.addOffer(offer)

            viewModel.viewState.observe(viewLifecycleOwner){
                when(it){
                     is Resource.Success -> {
                        requireActivity()
                            .findViewById<FragmentContainerView>(R.id.nav_host_fragment_content_main)
                            .findNavController()
                            .navigate(R.id.action_useNewOffer_to_useHome)
                    }
                    is Resource.Error -> TODO()
                    is Resource.Loading -> {
                        binding.addOfferContainer.visibility = View.GONE
                        binding.progressBarCyclic.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}