package com.francis.stockmarketapp.stockfeature.di

import com.francis.stockmarketapp.stockfeature.data.csv.CSVParser
import com.francis.stockmarketapp.stockfeature.data.csv.CompanyListingsParser
import com.francis.stockmarketapp.stockfeature.data.csv.IntradayInfoParser
import com.francis.stockmarketapp.stockfeature.data.repository.StockRepositoryImpl
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyListing
import com.francis.stockmarketapp.stockfeature.domain.model.IntradayInfo
import com.francis.stockmarketapp.stockfeature.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds // for abstract function instead of @Provides
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds // for abstract function instead of @Provides
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>


    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}