package com.adrianbadarau.dto

import com.adrianbadarau.models.InventoryItem

data class InventoryGetResponse(
        val inventory: MutableList<InventoryItem>
)