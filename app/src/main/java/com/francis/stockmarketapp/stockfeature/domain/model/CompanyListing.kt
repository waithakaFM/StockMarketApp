package com.francis.stockmarketapp.stockfeature.domain.model

/**
 * Use it to show them in the UI.
 * It is independent of what kind of library we use in our data layer.
 * In case we change the library in the data layer we won't change this.
 */
data class CompanyListing(
    val name: String,
    val symbol: String,
    val exchange: String
)