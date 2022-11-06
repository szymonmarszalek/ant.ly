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
import com.example.antly.data.dto.Offer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddOfferFragment : Fragment() {

    private val viewModel: AddNewOfferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_offer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subjectTitle = view.findViewById<EditText>(R.id.subjectTitle)
        val subjectDescription = view.findViewById<EditText>(R.id.subjectDescription)
        val subjectCategory = view.findViewById<EditText>(R.id.subjectCategory)
        val price = view.findViewById<EditText>(R.id.price)
        val addOfferButton = view.findViewById<Button>(R.id.addOffer)

        addOfferButton.setOnClickListener {
           var offer = Offer(subject = subjectCategory.text.toString(),
               description = subjectDescription.text.toString(),
               price = price.text.toString().toInt(),
               title = subjectTitle.text.toString()
           )
            println(subjectTitle.text.toString())

            viewModel.addOffer(offer!!)
        }
    }
}