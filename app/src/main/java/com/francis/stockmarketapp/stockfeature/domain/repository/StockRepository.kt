package com.francis.stockmarketapp.stockfeature.domain.repository

import com.francis.stockmarketapp.stockfeature.domain.model.CompanyInfo
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyListing
import com.francis.stockmarketapp.stockfeature.domain.model.IntradayInfo
import com.francis.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Domain layer should not access the data layer!!
 * That's why we will return list of CompanyListing rather than CompanyListingEntity
 */
interface StockRepository {

    // We use Flow to emit different results over a period of time - from cache to UI, from API
    // to updating the cache
    suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}