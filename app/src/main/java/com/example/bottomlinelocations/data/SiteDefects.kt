package com.example.bottomlinelocations.data

data class SiteDefects(
    val id: String = "Placeholder",
    val siteName: String,
    val address: String,
    val zipcode: String,
    val city: String,
    val description: String,
    val timestamp: String,
    var picId: String
)
