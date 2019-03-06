package org.revcrm.graphql.creating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithStringList
import org.revcrm.testdb.deleteStringListData

class CreatingWithEmbeddedListTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            deleteStringListData(ds)
        }
    }

    @Nested
    inner class CreateEmbeddedList {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithStringListInput!) {
                    createTestWithStringList (data: $data) {
                        result {
                            id
                            name
                            values
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                code
                            }
                            fieldErrors {
                                code
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "data" to mapOf(
                    "name" to "Test Name",
                    "values" to listOf("value1", "value2")
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithStringList", TestWithStringList::class.java)

        @Test
        fun `returns new record data and ID`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Test Name")
            assertThat(result.result.values).contains("value1", "value2")
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `new record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithStringList::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Test Name")
                assertThat(match.values).contains("value1", "value2")
            }
        }
    }

    @Nested
    inner class EmbeddedListValidationError {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithStringListInput!) {
                    createTestWithStringList (data: $data) {
                        result {
                            id
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                code
                            }
                            fieldErrors {
                                entity
                                fieldPath
                                code
                                message
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "data" to mapOf(
                    "name" to "invalid record",
                    "values" to listOf<String>()
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithStringList", TestWithStringList::class.java)

        @Test
        fun `does not return result`() {
            assertThat(result.result).isNull()
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors.size).isGreaterThan(0)
            assertThat(result.validation.fieldErrors).anyMatch { err ->
                err.entity == "TestWithStringList" &&
                    err.fieldPath == "values" &&
                    err.code == "NotEmpty" &&
                    err.message.contains("must not be empty")
            }
        }

        @Test
        fun `new record is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(TestWithStringList::class.java)
                    .field("name").equal("invalid record")
                    .get()
                assertThat(match).isNull()
            }
        }
    }
}