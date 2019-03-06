package org.revcrm.graphql.deleting

import com.mongodb.BasicDBObject
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLDeleteResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithValidatedDelete

class DeletingDataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    fun removeTestData() {
        testDB.withDB { ds ->
            val col = ds.getCollection(TestWithValidatedDelete::class.java)
            col.remove(BasicDBObject())
        }
    }

    fun createTestRecord(status: String): ObjectId {
        val record = TestWithValidatedDelete(
            status = status
        )
        testDB.withDB { ds ->
            ds.save(record)
        }
        return record.id!!
    }

    @Nested
    inner class DeleteRecordPassesValidation {
        val recordId: ObjectId

        init {
            removeTestData()
            recordId = createTestRecord("all_good")
        }

        val id = "\$id"
        val res = api.query(
            """
                mutation ($id: ID!) {
                    deleteTestWithValidatedDelete (id: $id) {
                        result
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
                "id" to recordId
            )
        )
        val result = mapGraphQLDeleteResult(res, "deleteTestWithValidatedDelete")

        @Test
        fun `Record deletion succeeds`() {
            assertThat(result.result).isTrue()
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `Record does not exist in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithValidatedDelete::class.java, recordId)
                assertThat(match).isNull()
            }
        }
    }

    @Nested
    inner class DeleteRecordFailsValidation {
        val recordId: ObjectId

        init {
            removeTestData()
            recordId = createTestRecord("no_delete")
        }

        val id = "\$id"
        val res = api.query(
            """
                mutation ($id: ID!) {
                    deleteTestWithValidatedDelete (id: $id) {
                        result
                        validation {
                            hasErrors
                            entityErrors {
                                entity
                                code
                                message
                            }
                            fieldErrors {
                                code
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "id" to recordId
            )
        )
        val result = mapGraphQLDeleteResult(res, "deleteTestWithValidatedDelete")

        @Test
        fun `result is false`() {
            assertThat(result.result).isFalse()
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.fieldErrors).hasSize(0)
            assertThat(result.validation.entityErrors).hasSize(1)
            assertThat(result.validation.entityErrors[0]).matches { err ->
                err.entity == "TestWithValidatedDelete" &&
                err.code == "Denied" &&
                err.message == "You cannot delete an entity in this status"
            }
        }

        @Test
        fun `record is not deleted`() {
            testDB.withDB { ds ->
                val match = ds.get(TestWithValidatedDelete::class.java, recordId)
                assertThat(match).isNotNull()
            }
        }
    }

    @Nested
    inner class RecordNotFound {

        init {
            removeTestData()
        }

        val id = "\$id"
        val res = api.query(
            """
                mutation ($id: ID!) {
                    deleteTestWithValidatedDelete (id: $id) {
                        result
                        validation {
                            hasErrors
                            entityErrors {
                                entity
                                code
                                message
                            }
                            fieldErrors {
                                code
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "id" to "aaaaaaaaaaaaaaaaaaaaaaaa"
            )
        )
        val result = mapGraphQLDeleteResult(res, "deleteTestWithValidatedDelete")

        @Test
        fun `result is false`() {
            assertThat(result.result).isFalse()
        }

        @Test
        fun `returns expected error`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.fieldErrors).hasSize(0)
            assertThat(result.validation.entityErrors).hasSize(1)
            assertThat(result.validation.entityErrors[0]).matches { err ->
                err.entity == "TestWithValidatedDelete" &&
                err.code == "IDNotFound" &&
                err.message.contains("the specified id was not found")
            }
        }
    }
}