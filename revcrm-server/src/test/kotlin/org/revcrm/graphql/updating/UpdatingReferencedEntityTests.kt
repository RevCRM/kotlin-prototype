package org.revcrm.graphql.updating

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TEST_REFERENCED_ENTITIES
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithReferencedEntity
import org.revcrm.testdb.resetReferencedEntityData

val referencedEntity1 = TEST_REFERENCED_ENTITIES[0]
val referencedEntity2 = TEST_REFERENCED_ENTITIES[1]

fun setUpReferencedEntityData(db: DBService): ObjectId {
    val entity = db.withDB { ds ->
        resetReferencedEntityData(ds)
        val entity = TestWithReferencedEntity(
            name = "Initial Name",
            otherEntity = referencedEntity1
        )
        ds.save(entity)
        entity
    }
    return entity.id!!
}

class UpdatingReferencedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        meta.initialise()
        api.initialise()
    }

    @Nested
    inner class UpdateReferencedEntityData {
        val recordId: ObjectId

        init {
            recordId = setUpReferencedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    updateTestWithReferencedEntity (data: $data) {
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
                    "id" to recordId,
                    "otherEntity" to mapOf(
                        "id" to referencedEntity2.id
                    )
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `updates the referenced entity id`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Initial Name")
            assertThat(result.result.otherEntity!!.id).isEqualTo(referencedEntity2.id)
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `we can read the updated record and linked entity from the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithReferencedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Initial Name")
                assertThat(match.otherEntity!!.id).isEqualTo(referencedEntity2.id)
                assertThat(match.otherEntity!!.label).isEqualTo(referencedEntity2.label)
            }
        }
    }

    @Nested
    inner class UpdateReferencedEntityToNull {
        val recordId: ObjectId

        init {
            recordId = setUpReferencedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    updateTestWithReferencedEntity (data: $data) {
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
                    "id" to recordId,
                    "otherEntity" to null
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `updates the referenced entity id`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Initial Name")
            assertThat(result.result.otherEntity).isNull()
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `linked entity is null in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithReferencedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Initial Name")
                assertThat(match.otherEntity).isNull()
            }
        }
    }

    @Nested
    inner class UpdateFieldsOtherThanReferencedEntity {
        val recordId: ObjectId

        init {
            recordId = setUpReferencedEntityData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithReferencedEntityInput!) {
                    updateTestWithReferencedEntity (data: $data) {
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
                    "id" to recordId,
                    "name" to "Updated Name"
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithReferencedEntity", TestWithReferencedEntity::class.java)

        @Test
        fun `does not replace embedded entity data`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Updated Name")
            assertThat(result.result.otherEntity!!.id).isEqualTo(referencedEntity1.id)
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
                val match = ds.get(TestWithReferencedEntity::class.java, result.result.id)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Updated Name")
                assertThat(match.otherEntity!!.id).isEqualTo(referencedEntity1.id)
            }
        }
    }
}