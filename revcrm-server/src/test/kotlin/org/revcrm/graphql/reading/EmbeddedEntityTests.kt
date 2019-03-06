package org.revcrm.graphql.reading

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLQueryResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TEST_EMBEDDED_ENTITIES
import org.revcrm.testdb.TEST_STRING_LISTS
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithEmbeddedEntityList
import org.revcrm.testdb.TestWithStringList
import org.revcrm.testdb.resetEmbeddedEntityData
import org.revcrm.testdb.resetStringListData

class EmbeddedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            resetEmbeddedEntityData(ds)
            resetStringListData(ds)
        }
    }

    @Nested
    inner class EmbeddedEntityList {

        val res = api.query("""
                query {
                    TestWithEmbeddedEntityList {
                        results {
                            label,
                            options {
                                value
                            }
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = mapGraphQLQueryResult(res, "TestWithEmbeddedEntityList", TestWithEmbeddedEntityList::class.java)

        @Test
        fun `returns all rows`() {
            assertThat(result.results).hasSize(TEST_EMBEDDED_ENTITIES.size)
            assertThat(result.results[0].label).isEqualTo(TEST_EMBEDDED_ENTITIES[0].label)
            assertThat(result.results[1].label).isEqualTo(TEST_EMBEDDED_ENTITIES[1].label)
            assertThat(result.results[2].label).isEqualTo(TEST_EMBEDDED_ENTITIES[2].label)
        }

        @Test
        fun `each row has expected embedded entity key`() {
            assertThat(result.results[0].options).isInstanceOf(List::class.java)
            assertThat(result.results[1].options).isInstanceOf(List::class.java)
            assertThat(result.results[2].options).isNull()
        }

        @Test
        fun `returns embedded entity data`() {
            assertThat(result.results[0].options).hasSize(TEST_EMBEDDED_ENTITIES[0].options!!.size)
            assertThat(result.results[0].options!![0].value).isEqualTo(TEST_EMBEDDED_ENTITIES[0].options!![0].value)
            assertThat(result.results[1].options).hasSize(TEST_EMBEDDED_ENTITIES[1].options!!.size)
            assertThat(result.results[1].options!![0].value).isEqualTo(TEST_EMBEDDED_ENTITIES[1].options!![0].value)
        }
    }

    @Nested
    inner class StringList {

        val res = api.query("""
                query {
                    TestWithStringList {
                        results {
                            id
                            name
                            values
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = mapGraphQLQueryResult(res, "TestWithStringList", TestWithStringList::class.java)

        @Test
        fun `returns all rows`() {
            assertThat(result.results).hasSize(TEST_STRING_LISTS.size)
        }

        @Test
        fun `each row has expected data`() {
            assertThat(result.results[0].name).isEqualTo(TEST_STRING_LISTS[0].name)
            assertThat(result.results[0].values).containsAll(TEST_STRING_LISTS[0].values)
            assertThat(result.results[1].name).isEqualTo(TEST_STRING_LISTS[1].name)
            assertThat(result.results[1].values).containsAll(TEST_STRING_LISTS[1].values)
        }
    }
}