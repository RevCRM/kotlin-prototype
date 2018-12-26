package org.revcrm.graphql

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import graphql.ExecutionResult
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TEST_ACCOUNTS
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.objectIdDeserializer
import org.revcrm.testdb.resetAccountData

class ReadingData {

    val testDB = TestDB.instance
    val api = APIService(testDB)
    val gson = GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()

    init {
        testDB.withDB { ds ->
            resetAccountData(ds)
        }
        api.initialise()
    }

    fun <T> getResults(res: ExecutionResult, entityKey: String, resultClass: Class<T>): EntitySearchResults<T> {
        assertThat(res.errors).hasSize(0)
        val entityData = res.getData<Map<String, Any>>()
        val tree = gson.toJsonTree(entityData.get(entityKey))
        return gson.fromJson(tree, TypeToken.getParameterized(
            EntitySearchResults::class.java,
            resultClass
        ).type)
    }

    @Nested
    inner class Query_NoArgs {

        val res = api.query("""
                query {
                    Account {
                        results {
                            id
                            name
                            email
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = getResults(res, "Account", Map::class.java)

        @Test
        fun `returns all rows`() {
            assertThat(result.results).hasSize(TEST_ACCOUNTS.size)
            assertThat(result.results[0].get("name")).isEqualTo(TEST_ACCOUNTS[0].name)
            assertThat(result.results[1].get("name")).isEqualTo(TEST_ACCOUNTS[1].name)
            assertThat(result.results[2].get("name")).isEqualTo(TEST_ACCOUNTS[2].name)
        }

        @Test
        fun `returns all selected fields`() {
            assertThat(result.results).hasSize(TEST_ACCOUNTS.size)
            assertThat(result.results[0].containsKey("id")).isTrue()
            assertThat(result.results[0].containsKey("name")).isTrue()
            assertThat(result.results[0].containsKey("email")).isTrue()
        }

        @Test
        fun `does not return other fields`() {
            assertThat(result.results).hasSize(TEST_ACCOUNTS.size)
            assertThat(result.results[0].containsKey("phone")).isFalse()
            assertThat(result.results[0].containsKey("rating")).isFalse()
        }
    }

    @Nested
    inner class Query_Ordering {

        val res = api.query("""
                query {
                    Account (orderBy: ["-name", "contact"]) {
                        results {
                            id
                            name
                            contact
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = getResults(res, "Account", Map::class.java)

        @Test
        fun `returns rows sorted by name then contact`() {
            assertThat(result.results[0].get("name")).isEqualTo(TEST_ACCOUNTS[3].name)
            assertThat(result.results[1].get("name")).isEqualTo(TEST_ACCOUNTS[2].name)
            assertThat(result.results[2].get("name")).isEqualTo(TEST_ACCOUNTS[0].name)
            assertThat(result.results[3].get("name")).isEqualTo(TEST_ACCOUNTS[1].name)
        }
    }

    @Nested
    inner class Query_Filtering {

        val res = api.query("""
                query {
                    Account (
                        where: {
                            _or: [
                                {contact: { _eq: "Joan"}},
                                {contact: { _eq: "Derek"}}
                            ]
                        }
                        orderBy: ["contact"]
                    ) {
                        results {
                            id
                            name
                            contact
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = getResults(res, "Account", Map::class.java)

        @Test
        fun `returns rows that match the specified "where" clause`() {
            assertThat(result.results.size).isEqualTo(2)
            assertThat(result.results[0].get("contact")).isEqualTo(TEST_ACCOUNTS[3].contact)
            assertThat(result.results[1].get("contact")).isEqualTo(TEST_ACCOUNTS[2].contact)
        }
    }
}