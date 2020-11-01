package com.adrianbadarau

import com.adrianbadarau.models.InventoryItem
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.ktor.jackson.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {


    @Test
    fun `with initial data inventory items route should return 4 items`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/inventory-item").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val items = jacksonObjectMapper().readValue(response.content!!, jacksonTypeRef<List<InventoryItem>>())
                assertEquals(4, items.size)
            }
        }
    }

    @Test
    fun `making a post to inventory route should add item`() {
        val newItem = InventoryItem(artId = "7", name = "test", stock = 5)
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/api/v1/inventory-item") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    jacksonObjectMapper().writeValueAsString(newItem)
                )
            }.apply {
                handleRequest(HttpMethod.Get, "/api/v1/inventory-item").apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    val items = jacksonObjectMapper().readValue(response.content!!, jacksonTypeRef<List<InventoryItem>>())
                    assertEquals(5, items.size)
                    assertTrue {
                        items.contains(newItem)
                    }
                }
            }
        }
    }

    @Test
    fun `calling the stock endpoint should give you the min stock for that item`(){
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/shopping/stock/Dinning%20Table%20YT").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("6", response.content)
            }
        }
    }
}
