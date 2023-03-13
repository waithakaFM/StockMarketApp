package com.francis.stockmarketapp.stockfeature.data.remote.dto

import com.squareup.moshi.Json

/**
 * Data transfer object
 * Kotlin representation of a JSON data
 */
data class CompanyInfoDto (
    // @field:Json is to tell that api has different name from the variable
    @field:Json(name= "Symbol") val symbol: String?,
    @field:Json(name= "Description") val description: String?,
    @field:Json(name= "Name") val name: String?,
    @field:Json(name= "Country") val country: String?,
    @field:Json(name= "Industry") val industry: String?
)