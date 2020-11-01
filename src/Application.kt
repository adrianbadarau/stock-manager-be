package com.adrianbadarau

import com.adrianbadarau.config.DIContainer
import com.adrianbadarau.dto.InventoryGetResponse
import com.adrianbadarau.dto.ProductsGetResponse
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.models.Product
import com.adrianbadarau.services.InventoryItemService
import com.adrianbadarau.services.ProductService
import com.adrianbadarau.services.ShoppingService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.jackson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val diContainer = loadDIContainer()
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(Authentication) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

/**
 * Here we create the DIContainer that will be passed throughout our application,
 * please feel free to add any other classes here
 */
fun loadDIContainer(): DIContainer {
    val diContainer = DIContainer();
    // here we cheat for a bit since it's a demo app
    val (initialItems, initialProducts) = loadInitialData()

    diContainer.add<InventoryItemService>(InventoryItemService(initialItems))
    diContainer.add<ProductService>(ProductService(initialProducts))
    diContainer.add<ShoppingService>(
        ShoppingService(
            inventoryItemService = diContainer.get(InventoryItemService::class) as InventoryItemService,
            productService = diContainer.get(ProductService::class) as ProductService
        )
    )

    return diContainer
}

fun loadInitialData(): Pair<MutableList<InventoryItem>, MutableList<Product>> {
    val inventoryItems = jacksonObjectMapper().readValue(
        Application::class.java.classLoader.getResource("inventory.json")?.readText(),
        InventoryGetResponse::class.java
    ).inventory
    val products = jacksonObjectMapper().readValue(Application::class.java.classLoader.getResource("products.json")?.readText(),
        ProductsGetResponse::class.java
    ).products
    return Pair(inventoryItems, products)
}

