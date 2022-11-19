package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
     val subject = MutableLiveData<kotlin.String>()
     val range = MutableLiveData<kotlin.String>()
     val localization = MutableLiveData<kotlin.String>()

     fun cleanValues() {
          subject.value = null
          range.value = null
          localization.value = null
     }
}