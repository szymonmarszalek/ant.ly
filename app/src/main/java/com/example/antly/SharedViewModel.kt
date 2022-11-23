package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
     val subject = MutableLiveData<String?>()
     val range = MutableLiveData<String?>()
     val localization = MutableLiveData<String?>()

     fun cleanValues() {
          subject.value = null
          range.value = null
          localization.value = null
     }
}