package com.adrianbadarau.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SalesOrderPostRequest(
        @JsonProperty("sale_items") val saleItems: List<Pair<String, Int>>
)
