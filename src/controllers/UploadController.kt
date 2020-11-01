package com.adrianbadarau.controllers

import com.adrianbadarau.config.DIContainer
import com.adrianbadarau.config.Messages
import com.adrianbadarau.dto.InventoryGetResponse
import com.adrianbadarau.dto.ProductsGetResponse
import com.adrianbadarau.services.InventoryItemService
import com.adrianbadarau.services.ProductService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.uploadRoutes(diContainer: DIContainer) {
    route("/api/v1/uploads") {
        post("/inventory-items") {
            var success = false
            call.receiveMultipart().forEachPart {
                if (it is PartData.FileItem) {
                    it.streamProvider().use { its ->
                        val readValues = jacksonObjectMapper().readValue(its, InventoryGetResponse::class.java)
                        val service = diContainer.get(InventoryItemService::class) as InventoryItemService
                        service.changeData(readValues.inventory)
                        success = true
                        // we only expect one file so we can exit
                        return@forEachPart
                    }
                }
            }
            if(success){
                call.respond(Messages.SUCCESS)
            } else {
                call.respond(HttpStatusCode.BadRequest, Messages.ERROR)
            }
        }

        post("/products") {
            var success = false
            call.receiveMultipart().forEachPart {
                if (it is PartData.FileItem) {
                    it.streamProvider().use { its ->
                        val readValues = jacksonObjectMapper().readValue(its, ProductsGetResponse::class.java)
                        val service = diContainer.get(ProductService::class) as ProductService
                        service.changeData(readValues.products)
                        success = true
                        // we only expect one file
                        return@forEachPart
                    }
                }
            }
            if(success){
                call.respond(Messages.SUCCESS)
            } else {
                call.respond(HttpStatusCode.BadRequest, Messages.ERROR)
            }
        }
    }
}
