package com.francis.stockmarketapp.stockfeature.data.mapper

import com.francis.stockmarketapp.stockfeature.data.local.CompanyListingEntity
import com.francis.stockmarketapp.stockfeature.data.remote.dto.CompanyInfoDto
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyInfo
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyListing

/**
 * How we take CompanyListingEntity and transform it to CompanyListing Model and VS
 */

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo{
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}