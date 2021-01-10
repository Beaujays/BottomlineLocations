package com.example.bottomlinelocations.data

import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toSiteDefect() = SiteDefects (
    id = this.id,
    siteName = this["siteName"] as String,
    address = this["address"] as String,
    zipcode = this["zipcode"] as String,
    city = this["city"] as String,
    description = this["remark"] as String,
    timestamp = this["timestamp"] as String,
    picId = this["imgSrcUrl"] as String
)

fun SiteDefects.toData() = mapOf (
    "siteName" to this.siteName,
    "address" to this.address,
    "zipcode" to this.zipcode,
    "city" to this.city,
    "remark" to this.description,
    "timestamp" to this.timestamp,
    "imgSrcUrl" to this.picId
)