package com.adrianbadarau.services

import com.adrianbadarau.config.Messages
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.ProductItem
import io.ktor.application.*
import io.ktor.response.*

class InventoryItemService(private var inventoryItems: MutableList<InventoryItem> = mutableListOf()) {

}
