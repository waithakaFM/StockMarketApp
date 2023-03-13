package com.francis.stockmarketapp.stockfeature.presentation.company_info

import com.francis.stockmarketapp.stockfeature.domain.model.CompanyInfo
import com.francis.stockmarketapp.stockfeature.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)