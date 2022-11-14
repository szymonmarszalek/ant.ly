package com.example.antly.data.dto

data class OfferResponse(
    var id: Int,
    var title: String,
    var descriptionShort: String,
    var descriptionLong: String,
    var subject: String,
    var location: String,
    var imageUrl: String,
    var price: Int,
    var teacherName: String,
    var range: String) {
}
