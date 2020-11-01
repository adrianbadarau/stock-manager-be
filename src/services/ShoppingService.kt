package com.adrianbadarau.services

import com.adrianbadarau.config.Messages
import com.adrianbadarau.dto.SalesOrderPostRequest
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.Product
import com.adrianbadarau.models.ProductItem

class ShoppingService(
        private val inventoryItemService: InventoryItemService,
        private val productService: ProductService
) {

}
