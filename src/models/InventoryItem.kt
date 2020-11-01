package com.adrianbadarau.models

import com.fasterxml.jackson.annotation.JsonProperty

data class InventoryItem(
        @JsonProperty("art_id")
        val artId: String,
        val name: String,
        val stock: Int
)