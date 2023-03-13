package com.francis.stockmarketapp.stockfeature.presentation.company_listings

import com.francis.stockmarketapp.stockfeature.domain.model.CompanyListing

data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)