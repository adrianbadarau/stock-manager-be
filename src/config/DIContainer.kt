package com.adrianbadarau.config

class DIContainer {
    val items: MutableMap<String, Any> = mutableMapOf()
    fun <T> get(className: T): Any {
        return items.getOrElse(className.toString()) { throw Exception("Class not found") }
    }

    fun <T> add(instance: Any) {
        items[instance::class.java.toString()] = instance
    }
}