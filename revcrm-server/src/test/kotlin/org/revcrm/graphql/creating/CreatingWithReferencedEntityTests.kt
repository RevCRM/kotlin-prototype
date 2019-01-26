package org.revcrm.graphql.creating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TEST_REFERENCED_ENTITIES
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestReferencedEntity
import org.revcrm.testdb.TestWithReferencedEntity
import org.revcrm.testdb.resetReferencedEntityData
import xyz.morphia.mapping.MappingException

class CreatingWithReferencedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            resetReferencedEntityData(ds)
        }
        meta.initialise()
        api.initialise()
    }

    val referencedEntity = TEST_REFERENCED_ENTITIES[0]

    @Nested
    inner class CreateWithReferencedEntity {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    createTestWithReferencedEntity (data: $data) {
                        result {
                            id
                            name
                            otherEntity {
                                id
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
                    "name" to "Test With Reference",
                    "otherEntity" to mapOf(
                        "id" to referencedEntity.id
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `returns new record data and ID`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Test With Reference")
            assertThat(result.result.otherEntity).isNotNull()
            assertThat(result.result.otherEntity!!.id).isEqualTo(referencedEntity.id)
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `we can retrieve the new record and the referenced record from the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithReferencedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Test With Reference")
                assertThat(match.otherEntity!!.label).isEqualTo(referencedEntity.label)
            }
        }

        @Test
        fun `the referenced record is not duplicated`() {
            testDB.withDB { ds ->
                val count = ds.createQuery(TestReferencedEntity::class.java)
                    .field("label").equal(referencedEntity.label)
                    .count()
                assertThat(count).isEqualTo(1)
            }
        }
    }

    @Nested
    inner class CreateWithIncorrectIDForReferencedEntity {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    createTestWithReferencedEntity (data: $data) {
                        result {
                            id
                            name
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
                    "name" to "Test With Invalid Reference",
                    "otherEntity" to mapOf(
                        "id" to "5c4abcd49096811a66bfaf13" // missing id
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `successfully creates record with missing referenced id`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Test With Invalid Reference")
        }

        @Test
        fun `does not return any validation errors`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `MappingException is raised when we try to read the record`() {
            assertThrows<MappingException> {
                testDB.withDB { ds ->
                    ds.get(TestWithReferencedEntity::class.java, result.result.id)
                }
            }
        }
    }

    @Nested
    inner class CreateWithAllowedIncorrectIdForReferencedEntity {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    createTestWithReferencedEntity (data: $data) {
                        result {
                            id
                            name
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
                    "name" to "Test With Missing Reference",
                    // missing id allowed by @Reference(ignoreMissing = true)
                    "maybeMissingEntity" to mapOf(
                        "id" to "5c4abcd49096811a66bfaf13"
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `successfully creates record with missing referenced id`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Test With Missing Reference")
        }

        @Test
        fun `does not return any validation errors`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `we can retrieve the new record and the missing referenced is null`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithReferencedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Test With Missing Reference")
                assertThat(match.maybeMissingEntity).isNull()
            }
        }
    }
}