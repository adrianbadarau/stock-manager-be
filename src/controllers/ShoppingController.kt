package com.adrianbadarau.controllers

import com.adrianbadarau.config.DIContainer
import com.adrianbadarau.dto.SalesOrderPostRequest
import com.adrianbadarau.services.ShoppingService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.shoppingRoutes(diContainer: DIContainer) {
    val service = diContainer.get(ShoppingService::class) as ShoppingService
    route("/api/v1/shopping") {
        post {
            val postItem = call.receive<SalesOrderPostRequest>()

            call.respond(
                service.sale(postItem)
            )
        }
        get("/stock/{productName}") {
            call.respond(service.getStock(call.parameters["productName"]!!))
        }
    }
}
