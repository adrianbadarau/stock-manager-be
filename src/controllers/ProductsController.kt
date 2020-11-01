package com.adrianbadarau.controllers

import com.adrianbadarau.config.DIContainer
import com.adrianbadarau.config.Messages
import com.adrianbadarau.models.Product
import com.adrianbadarau.services.ProductService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.productRoutes(diContainer: DIContainer) {
    val service = diContainer.get(ProductService::class) as ProductService
    route("/api/v1/products") {
        get("") {
            call.respond(service.getAll())
        }

        get("/{name}") {
            if (call.parameters["name"] == null) {
                return@get call.respond(HttpStatusCode.BadRequest, Messages.ERROR)
            }
            service.find(name = call.parameters["name"]!!)?.let { foundProduct ->
                return@get call.respond(foundProduct)
            }
            call.respond(HttpStatusCode.NotFound, Messages.NOT_FOUND)
        }

        post {
            val newProduct = call.receive<Product>()
            service.saveOrUpdate(newProduct)
            call.respond(Messages.SUCCESS)
        }

        delete("/{name}") {
            try {
                call.respond(service.remove(call.parameters["name"]!!))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }
    }
}
