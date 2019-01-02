package org.revcrm.graphql.schema

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GetWhereTests {

    @Test
    fun `transforms "_someKey" into "$someKey"`() {
        val query = mutableMapOf<Any, Any>(
            "name" to mutableMapOf("_eq" to "Russell")
        )
        val expectedQuery = mapOf(
            "name" to mapOf("\$eq" to "Russell")
        )
        convertOperators(query)
        assertThat(query).isEqualTo(expectedQuery)
    }

    @Test
    fun `transforms "_someKey" into "$someKey" in nested lists`() {
        val query = mutableMapOf<Any, Any>(
            "_or" to listOf(
                mutableMapOf("name" to mutableMapOf("_eq" to "Russell")),
                mutableMapOf("name" to mutableMapOf("_eq" to "Briggsy"))
            )
        )
        val expectedQuery = mapOf(
            "\$or" to listOf(
                mapOf("name" to mapOf("\$eq" to "Russell")),
                mapOf("name" to mapOf("\$eq" to "Briggsy"))
            )
        )
        convertOperators(query)
        assertThat(query).isEqualTo(expectedQuery)
    }

    @Nested
    inner class ObjectIDFields {

        @Test
        fun `converts an "id" key with a 24-character string value to an ObjectID`() {
            val query = mutableMapOf<Any, Any>(
                "id" to "5c22c9316029ad9803aba898"
            )
            val expectedQuery = mapOf(
                "_id" to ObjectId("5c22c9316029ad9803aba898")
            )
            convertOperators(query)
            assertThat(query).isEqualTo(expectedQuery)
        }

        @Test
        fun `converts an "_id" key with a 24-character string value to an ObjectID`() {
            val query = mutableMapOf<Any, Any>(
                "_id" to "5c22c9316029ad9803aba898"
            )
            val expectedQuery = mapOf(
                "_id" to ObjectId("5c22c9316029ad9803aba898")
            )
            convertOperators(query)
            assertThat(query).isEqualTo(expectedQuery)
        }

        @Test
        fun `does not convert an "id" key with a non-24-character string value to an ObjectID`() {
            val query = mutableMapOf<Any, Any>(
                "id" to "5c9811"
            )
            val expectedQuery = mapOf(
                "id" to "5c9811"
            )
            convertOperators(query)
            assertThat(query).isEqualTo(expectedQuery)
        }
    }
}