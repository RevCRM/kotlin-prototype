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
        fun `returns entities with expected isEmbedded setting`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.isEmbedded).isFalse()
            assertThat(meta.getEntity("TestEntity2")!!.isEmbedded).isFalse()
            assertThat(meta.getEntity("ParentEntity")).isNull()
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.isEmbedded).isTrue()
        }

        @Test
        fun `apiEnabled defaults to true`() {
            assertThat(meta.getEntity("TestFieldsEntity")!!.isApiEnabled).isTrue()
            assertThat(meta.getEntity("TestEntity2")!!.isApiEnabled).isTrue()
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
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.isEmbedded).isTrue()
            assertThat(meta.getEntity("TestFieldsEntity")!!.isEmbedded).isFalse()
        }

        @Test
        fun `entity name matches name`() {
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.name).isEqualTo("TestEmbeddedEntity")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.className).isEqualTo("org.revcrm.testdb.TestEmbeddedEntity")
        }

        @Test
        fun `returns Base entity fields`() {
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(meta.getEntity("TestEmbeddedEntity")!!.fields)
                .containsKey("value")
        }
    }

    @Nested
    inner class SensitiveEntities {

        @Test
        fun `entities annotated with @APIDisabled have apiEnabled = false`() {
            assertThat(meta.getEntity("SensitiveEntity")!!.isApiEnabled).isFalse()
        }
    }
}
