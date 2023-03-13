package com.francis.stockmarketapp.stockfeature.data.repository

import com.francis.stockmarketapp.stockfeature.data.csv.CSVParser
import com.francis.stockmarketapp.stockfeature.data.csv.IntradayInfoParser
import com.francis.stockmarketapp.stockfeature.data.local.StockDatabase
import com.francis.stockmarketapp.stockfeature.data.mapper.toCompanyInfo
import com.francis.stockmarketapp.stockfeature.data.mapper.toCompanyListing
import com.francis.stockmarketapp.stockfeature.data.mapper.toCompanyListingEntity
import com.francis.stockmarketapp.stockfeature.data.remote.StockApi
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyInfo
import com.francis.stockmarketapp.stockfeature.domain.model.CompanyListing
import com.francis.stockmarketapp.stockfeature.domain.model.IntradayInfo
import com.francis.stockmarketapp.stockfeature.domain.repository.StockRepository
import com.francis.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't find data"))
                null
            } catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load the data try again later"))
                null
            }

            /**
             * load data from api and update our cache
             */
            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                // Make database our single source of truth
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {

            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)

        } catch (e: IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday data"
            )
        } catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday data"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load companyInfo data"
            )
        } catch (e: HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load companyInfo data"
            )
        }
    }
}