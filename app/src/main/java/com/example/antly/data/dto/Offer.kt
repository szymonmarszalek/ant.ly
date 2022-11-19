package com.example.antly.data.dto

import kotlin.String

data class Offer(
    var title: String,
    var descriptionShort: String,
    var descriptionLong: String,
    var subject: String,
    var location: String,
    var imageUrl: String,
    var price: Int,
    var range: String
)

