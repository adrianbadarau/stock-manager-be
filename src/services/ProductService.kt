package com.adrianbadarau.services

import com.adrianbadarau.config.Messages
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.Product

class ProductService(private var products: MutableList<Product> = mutableListOf()) {
    fun getAll(): MutableList<Product> {
        return products
    }

    fun find(name: String): Product? {
        return products.firstOrNull {
            it.name == name
        }
    }

    fun saveOrUpdate(item: Product) {
        val indexOf = products.indexOfFirst { it.name == item.name }
        if (indexOf >= 0) {
            products.removeAt(indexOf)
        }
        products.add(item)
    }

    fun remove(name: String): Product {
        // first we check if we have that product in the list
        val indexOf = products.indexOfFirst { it.name == name }
        if (indexOf >= 0) {
            return products.removeAt(indexOf)
        }
        throw Exception(Messages.NOT_FOUND.text)
    }

    //This is a method to trigger a complete data change in a real scenario this would be much more complex and done within a transaction
    fun changeData(items:MutableList<Product>){
        this.products = items
    }
}
