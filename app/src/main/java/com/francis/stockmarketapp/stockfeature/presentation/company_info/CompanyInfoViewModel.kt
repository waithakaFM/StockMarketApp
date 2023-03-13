package com.francis.stockmarketapp.stockfeature.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francis.stockmarketapp.stockfeature.domain.repository.StockRepository
import com.francis.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    // to get access to navigation argument in the VM directly
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
       viewModelScope.launch {
           val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
           state = state.copy(isLoading = true)
           // launch new coroutine here because two api calls can't be called on the same coroutine
           // thread ( actually they can but for good practices we create other threads for each call)
           val companyInfoResult = async { repository.getCompanyInfo(symbol) }
           val intradayInfoResult = async { repository.getIntradayInfo(symbol) }

           // as soon as the coroutine is ready
           when(val result = companyInfoResult.await()){
               is Resource.Success -> {
                   state = state.copy(
                       company = result.data,
                       isLoading = false,
                       error = null
                   )
               }
               is Resource.Error -> {
                   state = state.copy(
                       isLoading = false,
                       error = result.message,
                       company = null
                   )
               }
               else -> Unit
           }

           when(val result = intradayInfoResult.await()){
               is Resource.Success -> {
                   state = state.copy(
                       stockInfos = result.data ?: emptyList(),
                       isLoading = false,
                       error = null
                   )
               }
               is Resource.Error -> {
                   state = state.copy(
                       isLoading = false,
                       error = result.message,
                       company = null
                   )
               }
               else -> Unit
           }
       }
    }
}