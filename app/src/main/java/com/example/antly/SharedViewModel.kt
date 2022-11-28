package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.antly.data.dto.OfferResponse

class SharedViewModel: ViewModel() {
     val subject = MutableLiveData<String?>()
     val range = MutableLiveData<String?>()
     val localization = MutableLiveData<String?>()
     var isPopBackStack = false
     var isSearchBarOpened = false
     var offerList = listOf<OfferResponse>()

     fun cleanValues() {
          subject.value = null
          range.value = null
          localization.value = null
     }
}