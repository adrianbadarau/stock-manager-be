package com.adrianbadarau.services

import com.adrianbadarau.config.Messages
import com.adrianbadarau.dto.SalesOrderPostRequest
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.Product
import com.adrianbadarau.models.ProductItem
import kotlin.math.min

class ShoppingService(
    private val inventoryItemService: InventoryItemService,
    private val productService: ProductService
) {
    /**
     * Here we will contain the logic for making a sale, we will check if the product still exists, and if we have
     * enough stock to fulfil the order
     * Since it's not specified we will expect an all or nothing scenario so if we don't have enough stock to fulfill
     * the entire order we will return an error
     */
    fun sale(salesOrderPostRequest: SalesOrderPostRequest): Messages {
        val orderItems = mutableListOf<Pair<Product, Int>>()
        salesOrderPostRequest.saleItems.forEach {
            val product = productService.find(it.first) ?: throw Exception(Messages.NOT_FOUND.text)
            orderItems.add(
                Pair(
                    product,
                    it.second
                )
            )
        }
        return inventoryItemService.handleSale(
            getUnifiedStock(orderItems)
        )

    }

    private fun getUnifiedStock(orderItems: List<Pair<Product, Int>>): List<ProductItem> {
        val inventoryRequirements = mutableListOf<ProductItem>()
        orderItems.forEach { pair ->
            val product = pair.first
            product.containArticles.forEach { productItem ->
                val indexOf = inventoryRequirements.indexOfFirst { it.artId == productItem.artId }
                val amount = productItem.amountOf * pair.second
                if (indexOf >= 0) {
                    val original = inventoryRequirements[indexOf]
                    inventoryRequirements[indexOf] = original.copy(amountOf = original.amountOf + amount)
                } else {
                    inventoryRequirements.add(productItem.copy(amountOf = amount))
                }
            }
        }
        return inventoryRequirements
    }

    fun getStock(productName: String): Int {
        var minStock = Int.MAX_VALUE
        productService.find(productName)?.let { product ->
            product.containArticles.forEach {
                val itemStock = inventoryItemService.find(it.artId)?.stock ?: 0
                val maxProductsForItem = itemStock.div(it.amountOf)
                if (minStock > maxProductsForItem) {
                    minStock = maxProductsForItem
                }
            }
        }
        return minStock
    }
}
