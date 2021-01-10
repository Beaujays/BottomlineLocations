package com.example.bottomlinelocations.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Sites(

    var id: Int,
    var siteName: String,
    val address: String,
    val zipcode: String,
    val city: String,
    var latLng: LatLng,
    val openingHours: String

): ClusterItem {
    /**
     * The position of this marker. This must always return the same value.
     */
    override fun getPosition(): LatLng = latLng

    /**
     * The title of this marker.
     */
    override fun getTitle(): String = siteName

    /**
     * The description of this marker.
     */
    override fun getSnippet(): String = "Openingstijden: $openingHours"
}

// Available sites to deliver
val allSites = listOf(
    Sites(
        1,
        "Tango Rosmalen",
        "Raadhuisstraat 38A",
        "5241BL",
        "Rosmalen",
        LatLng(51.716333, 5.356875),
        "24/7"
    ),
    Sites(
        2,
        "Avia Rosmalen",
        "Kloosterstraat 1",
        "5248NV",
        "Rosmalen",
        LatLng(51.700965, 5.362977),
        "Ma-Vrij 07:00 - 19:00"
    ),
    Sites(
        3,
        "Esso Rosmalen",
        "Molenstraat 9",
        "5242HA",
        "Rosmalen",
        LatLng(51.712118, 5.361643),
        "Ma-Zat 04:00 - 20:00"
    ),
    Sites(
        4,
        "Total Rosmalen",
        "Westeind 2",
        "5245NL",
        "Rosmalen",
        LatLng(51.722919, 5.350244),
        "Ma-Zat 04:00 - 20:00"
    )
)
