package org.revcrm.graphql.creating

import com.mongodb.BasicDBObject
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestEmbeddedEntity
import org.revcrm.testdb.TestWithEmbeddedEntity

fun setUpEmbeddedEntityData(db: DBService): ObjectId {
    val entity = TestWithEmbeddedEntity(
        label = "Initial Label",
        embedded = TestEmbeddedEntity(
            name = "Initial Embedded Name",
            value = "Initial Embedded Value"
        )
    )
    db.withDB { ds ->
        val col = ds.getCollection(TestWithEmbeddedEntity::class.java)
        col.remove(BasicDBObject())
        ds.save(entity)
    }
    return entity.id!!
}

class UpdatingEmbeddedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        meta.initialise()
        api.initialise()
    }

    @Nested
    inner class UpdateEmbeddedEntityData {
        val recordId: ObjectId

        init {
            recordId = setUpEmbeddedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithEmbeddedEntityInput!) {
                    updateTestWithEmbeddedEntity (data: $data) {
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
                    "id" to recordId,
                    "embedded" to mapOf(
                        "name" to "Updated Embedded Name"
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithEmbeddedEntity", TestWithEmbeddedEntity::class.java)

        @Test
        fun `completely replaces embedded entity data`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.label).isEqualTo("Initial Label")
            assertThat(result.result.embedded!!.name).isEqualTo("Updated Embedded Name")
            assertThat(result.result.embedded!!.value).isNull() // existing embedded entity is overwritten
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `updated record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithEmbeddedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.label).isEqualTo("Initial Label")
                assertThat(match.embedded!!.name).isEqualTo("Updated Embedded Name")
                assertThat(match.embedded!!.value).isNull()
            }
        }
    }

    @Nested
    inner class UpdateFieldsOtherThanEmbeddedEntity {
        val recordId: ObjectId

        init {
            recordId = setUpEmbeddedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithEmbeddedEntityInput!) {
                    updateTestWithEmbeddedEntity (data: $data) {
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
                    "id" to recordId,
                    "label" to "Updated Label"
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithEmbeddedEntity", TestWithEmbeddedEntity::class.java)

        @Test
        fun `does not replace embedded entity data`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.label).isEqualTo("Updated Label")
            assertThat(result.result.embedded!!.name).isEqualTo("Initial Embedded Name")
            assertThat(result.result.embedded!!.value).isEqualTo("Initial Embedded Value")
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `updated record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithEmbeddedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.label).isEqualTo("Updated Label")
                assertThat(match.embedded!!.name).isEqualTo("Initial Embedded Name")
            }
        }
    }

    @Nested
    inner class UpdateEmbeddedEntityValidation {
        val recordId: ObjectId

        init {
            recordId = setUpEmbeddedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithEmbeddedEntityInput!) {
                    updateTestWithEmbeddedEntity (data: $data) {
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
                    "id" to recordId,
                    "label" to "Updated Label",
                    "embedded" to mapOf(
                        "name" to ""
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithEmbeddedEntity", TestWithEmbeddedEntity::class.java)

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
        fun `changed data is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithEmbeddedEntity::class.java, recordId)
                assertThat(match.label).isEqualTo("Initial Label")
            }
        }
    }
}