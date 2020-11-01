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
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

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
}
