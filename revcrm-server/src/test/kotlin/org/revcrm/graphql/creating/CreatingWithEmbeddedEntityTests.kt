package org.revcrm.graphql.creating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithEmbeddedEntity
import org.revcrm.testdb.deleteStringListData

class CreatingWithEmbeddedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            deleteStringListData(ds)
        }
        meta.initialise()
        api.initialise()
    }

    @Nested
    inner class CreateWithEmbeddedEntity {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithEmbeddedEntityInput!) {
                    createTestWithEmbeddedEntity (data: $data) {
                        result {
                            id
                            label
                            embedded {
                                name
                                value
                            }
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
                    "label" to "Test With Embedded",
                    "embedded" to mapOf(
                        "name" to "Embedded",
                        "value" to "100"
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithEmbeddedEntity", TestWithEmbeddedEntity::class.java)

        @Test
        fun `returns new record data and ID`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.label).isEqualTo("Test With Embedded")
            assertThat(result.result.embedded).isNotNull()
            assertThat(result.result.embedded!!.name).isEqualTo("Embedded")
            assertThat(result.result.embedded!!.value).isEqualTo("100")
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
                val match = ds.get(TestWithEmbeddedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.label).isEqualTo("Test With Embedded")
                assertThat(match.embedded!!.name).isEqualTo("Embedded")
            }
        }
    }

    @Nested
    inner class EmbeddedEntityValidationError {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithEmbeddedEntityInput!) {
                    createTestWithEmbeddedEntity (data: $data) {
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
                    "label" to "invalid record",
                    "embedded" to mapOf(
                        "name" to ""
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithEmbeddedEntity", TestWithEmbeddedEntity::class.java)

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
                err.entity == "TestEmbeddedEntity" &&
                    err.fieldPath == "embedded.name" &&
                    err.code == "NotBlank" &&
                    err.message.contains("must not be blank")
            }
        }

        @Test
        fun `new record is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(TestWithEmbeddedEntity::class.java)
                    .field("label").equal("invalid record")
                    .get()
                assertThat(match).isNull()
            }
        }
    }
}