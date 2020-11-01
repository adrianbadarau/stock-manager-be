package com.adrianbadarau.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Product (
    val name:String,
    @JsonProperty("contain_articles")
    val containArticles:List<ProductItem>
)