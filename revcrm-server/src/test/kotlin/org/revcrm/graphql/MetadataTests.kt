package org.revcrm.graphql

import com.google.gson.reflect.TypeToken
import graphql.ExecutionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.getTestGson
import org.revcrm.testdb.numberOfApiDisabledEntities

class EntityMetadataInfo(
    val name: String,
    val fields: List<EntityFieldInfo>
)

class EntityFieldInfo(
    val name: String
)

class MetadataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)
    val gson = getTestGson()

    init {
        api.initialise()
    }

    fun getResults(res: ExecutionResult): List<EntityMetadataInfo> {
        assertThat(res.errors).hasSize(0)
        val data = res.getData<Map<String, Map<String, Any>>>()
        val metadata = data.get("Metadata")!!
        val tree = gson.toJsonTree(metadata.get("entities"))
        return gson.fromJson(tree, TypeToken.getParameterized(
            List::class.java,
            EntityMetadataInfo::class.java
        ).type)
    }

    @Nested
    inner class EntityMetadata {

        val res = api.query("""
            query {
                Metadata {
                    entities {
                        name
                        fields {
                            name
                        }
                    }
                }
            }
            """.trimIndent(), mapOf())
        val result = getResults(res)
        val entities = meta.getEntities()

        @Test
        fun `returns metadata for all registered entities`() {
            assertThat(entities.size).isGreaterThan(0)
            assertThat(result).hasSize(entities.size - numberOfApiDisabledEntities)
        }

        @Test
        fun `does not include entities where apiEnabled = false`() {
            val disabledEntity = entities.find { it.apiEnabled == false }!!
            assertThat(result).noneMatch { it.name == disabledEntity.name }
        }

        @Test
        fun `entity metadata matches entities from MetadataService`() {
            // NB: Assumes that the first entity is does not have apiDisabled = true
            val entityInfo = result[0]
            assertThat(entityInfo.name).isEqualTo(entities[0].name)
            assertThat(entityInfo.fields).hasSize(entities[0].fieldsList.size)
            assertThat(entityInfo.fields[0].name).isEqualTo(entities[0].fieldsList[0].name)
        }
    }
}