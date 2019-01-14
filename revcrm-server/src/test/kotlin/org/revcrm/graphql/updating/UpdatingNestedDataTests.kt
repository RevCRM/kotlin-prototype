package org.revcrm.graphql.creating

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithStringList
import org.revcrm.testdb.deleteStringListData

fun setUpData(db: DBService): ObjectId {
    val entity = TestWithStringList(
        name = "Initial Name",
        values = listOf("value1", "value2")
    )
    db.withDB { ds ->
        deleteStringListData(ds)
        ds.save(entity)
    }
    return entity.id!!
}

class UpdatingNestedDataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        meta.initialise()
        api.initialise()
    }

    @Nested
    inner class UpdateNestedData {
        val recordId: ObjectId

        init {
            recordId = setUpData(testDB)
        }

        val data = "\$data"
        val res = api.query("""
                mutation ($data: TestWithStringListInput!) {
                    updateTestWithStringList (data: $data) {
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
                    "id" to recordId,
                    "values" to listOf("value3", "value4")
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateTestWithStringList", TestWithStringList::class.java)

        @Test
        fun `completely replaces embedded list data`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Initial Name")
            assertThat(result.result.values).hasSize(2)
            assertThat(result.result.values).contains("value3", "value4")
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
                assertThat(match.name).isEqualTo("Initial Name")
                assertThat(match.values).contains("value3", "value4")
            }
        }
    }
}