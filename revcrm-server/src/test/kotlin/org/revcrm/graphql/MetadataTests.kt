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
import org.revcrm.testdb.testFieldsEntityDisabledFields

class EntityMetadataInfo(
    val name: String,
    val idField: String,
    val fields: List<EntityFieldInfo>,
    val isEmbedded: Boolean
)

class EntityFieldInfo(
    val name: String,
    val label: String,
    val type: String,
    val nullable: Boolean,
    val readonly: Boolean,
    val properties: Map<String, String>,
    val constraints: Map<String, String>
)

class MetadataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)
    val gson = getTestGson()

    init {
        meta.initialise()
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
                        idField
                        fields {
                            name
                            label
                            type
                            nullable
                            readonly
                            properties
                            constraints
                        }
                        isEmbedded
                    }
                }
            }
            """.trimIndent(), mapOf())
        val result = getResults(res)
        val entities = meta.getAllEntities()

        @Test
        fun `returns metadata for all registered entities`() {
            assertThat(entities.size).isGreaterThan(0)
            assertThat(result).hasSize(entities.size - numberOfApiDisabledEntities)
        }

        @Test
        fun `does not include entities where apiEnabled = false`() {
            val disabledEntity = entities.find { it.isApiEnabled == false }!!
            assertThat(result).noneMatch { it.name == disabledEntity.name }
        }

        @Test
        fun `does not include fields where apiEnabled = false`() {
            val entityWithDisabledField = result.find { it.name == "TestFieldsEntity" }!!
            assertThat(entityWithDisabledField.fields).noneMatch { it.name == "api_disabled_field" }
        }

        @Test
        fun `entity metadata matches entities from MetadataService`() {
            val entityInfo = entities.find { it.name == "TestFieldsEntity" }!!
            var resultInfo = result.find { it.name == "TestFieldsEntity" }!!
            assertThat(resultInfo.name).isEqualTo(entityInfo.name)
            assertThat(resultInfo.idField).isEqualTo(entityInfo.idField!!.name)
            assertThat(resultInfo.fields).hasSize(entityInfo.fields.size - testFieldsEntityDisabledFields)
            assertThat(resultInfo.isEmbedded).isFalse()

            var entityFieldInfo = entityInfo.fields["string_field"]!!
            var resultFieldInfo = resultInfo.fields.find { it.name == "string_field" }!!
            assertThat(resultFieldInfo.name).isEqualTo(entityFieldInfo.name)
            assertThat(resultFieldInfo.label).isEqualTo(entityFieldInfo.label)
            assertThat(resultFieldInfo.type).isEqualTo(entityFieldInfo.type)
            assertThat(resultFieldInfo.nullable).isEqualTo(entityFieldInfo.nullable)
            assertThat(resultFieldInfo.readonly).isEqualTo(entityFieldInfo.readonly)
        }

        @Test
        fun `includes field properties and constraints as JSON`() {
            val constraintsEntityMeta = result.find { it.name == "TestConstraintsEntity" }!!
            val field = constraintsEntityMeta.fields.find { it.name == "textField" }!!
            assertThat(field.properties.get("MultiLine")).isEqualTo("true")
            assertThat(field.constraints.get("NotBlank")).isEqualTo("true")
            assertThat(field.constraints.get("SizeMin")).isEqualTo("1")
            assertThat(field.constraints.get("SizeMax")).isEqualTo("10")
        }

        @Test
        fun `Embedded entities have isEmbedded = true`() {
            var resultInfo = result.find { it.name == "TestEmbeddedEntity" }!!
            assertThat(resultInfo.isEmbedded).isTrue()
        }
    }
}