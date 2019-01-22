package org.revcrm.meta

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB
import org.assertj.core.api.Assertions.assertThat

class EntityMetadataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)

    init {
        meta.initialise()
    }

    @Nested
    inner class EntityMetadata {

        @Test
        fun `returns only entities in the entityClasses list`() {
            assertThat(meta.getEntity("TestFieldsEntity")).isNotNull()
            assertThat(meta.getEntity("TestEntity2")).isNotNull()
            assertThat(meta.getEntity("ParentEntity")).isNull()
            assertThat(meta.getEntity("TestEmbeddedEntity")).isNull()
        }

        @Test
        fun `apiEnabled defaults to true`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.apiEnabled).isTrue()
            assertThat(meta.getEntity("TestEntity2")!!.apiEnabled).isTrue()
        }

        @Test
        fun `entity name matches name`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.name).isEqualTo("TestFieldsEntity")
            assertThat(meta.getEntity("TestEntity2")!!.name).isEqualTo("TestEntity2")
        }

        @Test
        fun `entity id field is set`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.idField!!.name).isEqualTo("id")
            assertThat(meta.getEntity("TestEntity2")!!.idField!!.name).isEqualTo("id")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.className).isEqualTo("org.revcrm.testdb.TestFieldsEntity")
            assertThat(meta.getEntity("TestEntity2")!!.className).isEqualTo("org.revcrm.testdb.TestEntity2")
        }

        @Test
        fun `returns Base entity fields`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.fields)
                .containsKey("string_field")
                .containsKey("int_field")
        }
    }

    @Nested
    inner class EmbeddedEntities {

        @Test
        fun `returns only entities in the embeddedClasses list`() {
            assertThat(meta.getEmbeddedEntity("TestEmbeddedEntity")).isNotNull()
            assertThat(meta.getEmbeddedEntity("TestFieldsEntity")).isNull()
        }

        @Test
        fun `entity name matches name`() {
            assertThat(meta.getEmbeddedEntity("TestEmbeddedEntity")!!.name).isEqualTo("TestEmbeddedEntity")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(meta.getEmbeddedEntity("TestEmbeddedEntity")!!.className).isEqualTo("org.revcrm.testdb.TestEmbeddedEntity")
        }

        @Test
        fun `returns Base entity fields`() {
            assertThat(meta.getEmbeddedEntity("TestEmbeddedEntity")!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(meta.getEmbeddedEntity("TestEmbeddedEntity")!!.fields)
                .containsKey("value")
        }
    }

    @Nested
    inner class SensitiveEntities {

        @Test
        fun `entities annotated with @APIDisabled have apiEnabled = false`() {
            assertThat(meta.getEntity("SensitiveEntity")!!.apiEnabled).isFalse()
        }
    }
}
