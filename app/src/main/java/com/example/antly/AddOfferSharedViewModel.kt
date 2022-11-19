package com.example.antly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddOfferSharedViewModel: ViewModel() {
    var offerTitle = MutableLiveData<String?>("")
    var offerSubject = MutableLiveData<String?>()
    var offerLevel = MutableLiveData<String?>()
    var offerImageUrl = MutableLiveData<String?>()
    var offerShortDescription = MutableLiveData<String?>()
    var offerLongDescription = MutableLiveData<String?>()
    var offerLocation = MutableLiveData<String?>()
    var offerPrice = MutableLiveData<Int?>()

    fun cleanFields() {
        offerTitle.value = null
        offerSubject.value = null
        offerLevel.value = null
        offerImageUrl.value = null
        offerShortDescription.value = null
        offerLongDescription.value = null
        offerLocation.value = null
        offerPrice.value = null
    }

}