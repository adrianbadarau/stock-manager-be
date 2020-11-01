package com.adrianbadarau.services

import com.adrianbadarau.config.Messages
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.ProductItem

class InventoryItemService(private var inventoryItems: MutableList<InventoryItem> = mutableListOf()) {
    fun findAll(): MutableList<InventoryItem> {
        return inventoryItems
    }

    fun find(id: String): InventoryItem? {
        return inventoryItems.firstOrNull() {
            it.artId == id
        }
    }

    fun saveOrUpdate(item: InventoryItem) {
        val indexOf = inventoryItems.indexOfFirst { it.artId == item.artId }
        if (indexOf >= 0) {
            inventoryItems.removeAt(indexOf)
        }
        inventoryItems.add(item)
    }

    fun remove(id: String, qty: Int): Boolean {
        var success = false;
        inventoryItems.replaceAll {
            if (it.artId == id && it.stock >= qty) {
                val copy = it.copy(
                    stock = it.stock - qty
                )
                success = true
                copy
            } else {
                it
            }
        }
        return success
    }

    /**
     * Here we check if we have the necessary inventory, since we don't have a DB and memory is important we do this loop
     * In a prod env with a db we would do the handle sale in a transaction and modify the stock accordingly
     */
    private fun haveInventory(items: List<ProductItem>): Boolean {
        items.forEach { productItem ->
            if (inventoryItems.first { it.artId == productItem.artId }.stock < productItem.amountOf) {
                return false
            }
        }
        return true
    }

    /**
     * In this method we handle the sale, first checking the inventory then making sure to update the stock
     */
    fun handleSale(items: List<ProductItem>): Messages {
        if (haveInventory(items)) {
            items.forEach {
                remove(it.artId, it.amountOf)
            }
            return Messages.SUCCESS
        } else {
            return Messages.NO_STOCK_ERROR
        }
    }

    //This is a method to trigger a complete data change in a real scenario this would be much more complex and done within a transaction
    fun changeData(items:MutableList<InventoryItem>){
        this.inventoryItems = items
    }
}
