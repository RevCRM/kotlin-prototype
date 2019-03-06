package org.revcrm.graphql.reading

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLQueryResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TEST_ENTITIES_WITH_REFERENCE
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithReferencedEntity
import org.revcrm.testdb.resetReferencedEntityData

class ReferencedEntityTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            resetReferencedEntityData(ds)
        }
    }

    val res = api.query("""
            query {
                TestWithReferencedEntity {
                    results {
                        id
                        name
                        otherEntity {
                            id
                            label
                        }
                    }
                }
            }
        """.trimIndent(), mapOf())
    val result = mapGraphQLQueryResult(res, "TestWithReferencedEntity", TestWithReferencedEntity::class.java)

    val testData = TEST_ENTITIES_WITH_REFERENCE

    @Test
    fun `returns all top-level records`() {
        assertThat(result.results).hasSize(testData.size)
        assertThat(result.results[0].name).isEqualTo(testData[0].name)
        assertThat(result.results[1].name).isEqualTo(testData[1].name)
        assertThat(result.results[2].name).isEqualTo(testData[2].name)
    }

    @Test
    fun `records have expected referenced entity data`() {
        assertThat(result.results[0].otherEntity!!.label).isEqualTo(testData[0].otherEntity!!.label)
        assertThat(result.results[1].otherEntity!!.label).isEqualTo(testData[1].otherEntity!!.label)
        assertThat(result.results[2].otherEntity).isNull()
    }
}