package com.adrianbadarau.controllers

import com.adrianbadarau.config.DIContainer
import com.adrianbadarau.config.Messages
import com.adrianbadarau.models.InventoryItem
import com.adrianbadarau.services.InventoryItemService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.inventoryRoutes(diContainer: DIContainer) {
    val service = diContainer.get(InventoryItemService::class) as InventoryItemService
    route("/api/v1/inventory-item") {
        get("") {
            call.respond(
                    service.findAll()
            )
        }
        get("/{id}") {
            service.find(call.parameters["id"]!!)?.let {
                return@get call.respond(it)
            }
            call.respond(Messages.ERROR.text)

        }
        post("/") {
            val postItem = call.receive<InventoryItem>()
            service.saveOrUpdate(item = postItem)
            call.respond(Messages.SUCCESS)
        }
        delete("/{id}/{qty}") {
            if (call.parameters["id"] == null || call.parameters["qty"] == null) {
                return@delete call.respond(HttpStatusCode.BadRequest, Messages.ERROR)
            }
            val success = service.remove(call.parameters["id"]!!, call.parameters["qty"]!!.toInt())
            if (success) {
                call.respond(Messages.SUCCESS)
            } else {
                call.respond(message = Messages.ERROR, status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
