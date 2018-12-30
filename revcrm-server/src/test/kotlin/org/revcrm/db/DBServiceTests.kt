package org.revcrm.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB

class DBServiceTests {

    val testDB = TestDB.instance
    val entities = testDB.getEntityMetadata().entities

    @Nested
    inner class EntityMetadata {

        @Test
        fun `returns only entities with @Entity annotation`() {
            assertThat(entities).containsKey("TestFieldsEntity")
            assertThat(entities).containsKey("TestEntity2")
            assertThat(entities).doesNotContainKey("ParentEntity")
        }

        @Test
        fun `apiEnabled defaults to true`() {
            assertThat(entities.get("TestFieldsEntity")!!.apiEnabled).isTrue()
            assertThat(entities.get("TestEntity2")!!.apiEnabled).isTrue()
        }

        @Test
        fun `entity name matches name`() {
            assertThat(entities["TestFieldsEntity"]!!.name).isEqualTo("TestFieldsEntity")
            assertThat(entities["TestEntity2"]!!.name).isEqualTo("TestEntity2")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(entities["TestFieldsEntity"]!!.className).isEqualTo("org.revcrm.testdb.TestFieldsEntity")
            assertThat(entities["TestEntity2"]!!.className).isEqualTo("org.revcrm.testdb.TestEntity2")
        }

        @Test
        fun `returns Base entity fields`() {
            assertThat(entities["TestFieldsEntity"]!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(entities["TestFieldsEntity"]!!.fields)
                .containsKey("string_field")
                .containsKey("int_field")
        }
    }

    @Nested
    inner class FieldMetadata_Types {

        val fieldsEntity = entities["TestFieldsEntity"]!!

        @Test
        fun `ObjectId field returns expected metadata`() {
            assertThat(fieldsEntity.fields["id"]!!.jvmType).isEqualTo("org.bson.types.ObjectId")
        }

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(fieldsEntity.fields["int_field"]!!.jvmType).isEqualTo("int")
        }

        @Test
        fun `Short field returns expected metadata`() {
            assertThat(fieldsEntity.fields["short_field"]!!.jvmType).isEqualTo("short")
        }

        @Test
        fun `Long field returns expected metadata`() {
            assertThat(fieldsEntity.fields["long_field"]!!.jvmType).isEqualTo("long")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(fieldsEntity.fields["float_field"]!!.jvmType).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(fieldsEntity.fields["double_field"]!!.jvmType).isEqualTo("double")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(fieldsEntity.fields["boolean_field"]!!.jvmType).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(fieldsEntity.fields["string_field"]!!.jvmType).isEqualTo("java.lang.String")
        }

        @Test
        fun `LocalDate field returns expected metadata`() {
            assertThat(fieldsEntity.fields["date_field"]!!.jvmType).isEqualTo("java.time.LocalDate")
        }

        @Test
        fun `LocalTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["time_field"]!!.jvmType).isEqualTo("java.time.LocalTime")
        }

        @Test
        fun `LocalDateTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["datetime_field"]!!.jvmType).isEqualTo("java.time.LocalDateTime")
        }

        @Test
        fun `Enum field returns expected metadata`() {
            assertThat(fieldsEntity.fields["enum_field"]!!.jvmType).isEqualTo("enum")
            assertThat(fieldsEntity.fields["enum_field"]!!.jvmSubtype).isEqualTo("org.revcrm.testdb.EnumFieldOptions")
        }
    }

    @Nested
    inner class FieldMetadata_Constraints {

        val constraintsEntity = entities["TestConstraintsEntity"]!!

        @Test
        fun `Non-nullable field returns expected metadata`() {
            assertThat(constraintsEntity.fields["non_nullable_field"]!!.nullable).isEqualTo(false)
        }

        @Test
        fun `Nullable field returns expected metadata`() {
            assertThat(constraintsEntity.fields["nullable_field"]!!.nullable).isEqualTo(true)
        }

        @Test
        fun `NotEmpty field returns expected metadata`() {
            assertThat(constraintsEntity.fields["notempty_field"]!!.constraints).containsEntry("NotEmpty", "true")
        }

        @Test
        fun `NotBlank field returns expected metadata`() {
            assertThat(constraintsEntity.fields["notblank_field"]!!.constraints).containsEntry("NotBlank", "true")
        }

        @Test
        fun `Min field returns expected metadata`() {
            assertThat(constraintsEntity.fields["min_field"]!!.constraints).containsEntry("Min", "10")
        }

        @Test
        fun `Max field returns expected metadata`() {
            assertThat(constraintsEntity.fields["max_field"]!!.constraints).containsEntry("Max", "100")
        }
    }

    @Nested
    inner class SensitiveEntities {

        @Test
        fun `entities annotated with @APIDisabled have apiEnabled = false`() {
            assertThat(entities.get("SensitiveEntity")!!.apiEnabled).isFalse()
        }
    }
}
