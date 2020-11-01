package com.adrianbadarau.models

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductItem (
        @JsonProperty("art_id")
    val artId: String,
        @JsonProperty("amount_of")
    val amountOf: Int
)