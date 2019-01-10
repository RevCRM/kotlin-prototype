package org.revcrm.graphql.reading

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TEST_ACCOUNTS
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestEntity2
import org.revcrm.testdb.resetAccountData
import org.revcrm.testdb.resetTestEntity2Data

class FilteringDataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            resetAccountData(ds)
            resetTestEntity2Data(ds)
        }
        meta.initialise()
        api.initialise()
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
        val result = mapGraphQLResult(res, "Account", Map::class.java)

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
        val result = mapGraphQLResult(res, "Account", Map::class.java)

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
                        meta {
                            totalCount
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = mapGraphQLResult(res, "Account", Map::class.java)

        @Test
        fun `returns rows that match the specified "where" clause`() {
            assertThat(result.results.size).isEqualTo(2)
            assertThat(result.meta.totalCount).isEqualTo(2)
            assertThat(result.results[0].get("contact")).isEqualTo(TEST_ACCOUNTS[3].contact)
            assertThat(result.results[1].get("contact")).isEqualTo(TEST_ACCOUNTS[2].contact)
        }
    }

    @Nested
    inner class Query_LimitAndOffset {

        val fields = "results { id name number } meta { limit offset totalCount }"

        val defaultLimit = 20 // hardcoded in APIContest.defaultResultsLimit at the moment

        @Test
        fun `by default we return the first "defaultResultsLimit" results`() {
            val res = api.query("""
                query {
                    TestEntity2 (orderBy: ["number"]) {
                        $fields
                    }
                }
            """.trimIndent(), mapOf())
            val result = mapGraphQLResult(res, "TestEntity2", TestEntity2::class.java)
            assertThat(result.results.size).isEqualTo(defaultLimit)
            assertThat(result.meta.limit).isEqualTo(defaultLimit)
            assertThat(result.meta.offset).isEqualTo(0)
            assertThat(result.meta.totalCount).isGreaterThan(defaultLimit.toLong())
            assertThat(result.results[0].number).isEqualTo(1)
            assertThat(result.results[19].number).isEqualTo(defaultLimit)
        }

        @Test
        fun `when "limit" is specified, we return the first n results`() {
            val res = api.query("""
                query {
                    TestEntity2 (
                        limit: 10,
                        orderBy: ["number"]
                    ) {
                        $fields
                    }
                }
            """.trimIndent(), mapOf())
            val result = mapGraphQLResult(res, "TestEntity2", TestEntity2::class.java)
            assertThat(result.results.size).isEqualTo(10)
            assertThat(result.meta.limit).isEqualTo(10)
            assertThat(result.meta.offset).isEqualTo(0)
            assertThat(result.meta.totalCount).isGreaterThan(20)
            assertThat(result.results[0].number).isEqualTo(1)
            assertThat(result.results[9].number).isEqualTo(10)
        }

        @Test
        fun `"limit" can be greater than totalCount`() {
            val res = api.query("""
                query {
                    TestEntity2 (
                        limit: 100,
                        orderBy: ["number"]
                    ) {
                        $fields
                    }
                }
            """.trimIndent(), mapOf())
            val result = mapGraphQLResult(res, "TestEntity2", TestEntity2::class.java)
            assertThat(result.results.size).isGreaterThan(20).isLessThan(100)
            assertThat(result.meta.limit).isEqualTo(100)
            assertThat(result.meta.offset).isEqualTo(0)
            assertThat(result.meta.totalCount).isGreaterThan(20)
            assertThat(result.results[0].number).isEqualTo(1)
            assertThat(result.results[20].number).isEqualTo(21)
        }

        @Test
        fun `I can use "offset" and "limit" to window the results`() {
            val res = api.query("""
                query {
                    TestEntity2 (
                        offset: 1,
                        limit: 5,
                        orderBy: ["number"]
                    ) {
                        $fields
                    }
                }
            """.trimIndent(), mapOf())
            val result = mapGraphQLResult(res, "TestEntity2", TestEntity2::class.java)
            assertThat(result.results.size).isEqualTo(5)
            assertThat(result.meta.limit).isEqualTo(5)
            assertThat(result.meta.offset).isEqualTo(1)
            assertThat(result.meta.totalCount).isGreaterThan(20)
            assertThat(result.results[0].number).isEqualTo(2)
            assertThat(result.results[4].number).isEqualTo(6)
        }
    }
}