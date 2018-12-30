package org.revcrm.meta

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB
import org.assertj.core.api.Assertions.assertThat
import org.revcrm.meta.fields.BooleanField
import org.revcrm.meta.fields.DateField
import org.revcrm.meta.fields.DateTimeField
import org.revcrm.meta.fields.EnumField
import org.revcrm.meta.fields.FloatField
import org.revcrm.meta.fields.IDField
import org.revcrm.meta.fields.IntegerField
import org.revcrm.meta.fields.TextField
import org.revcrm.meta.fields.TimeField

class MetadataServiceTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)

    @Nested
    inner class EntityMetadata {

        @Test
        fun `returns only entities with @Entity annotation`() {
            assertThat(meta.getEntity("TestFieldsEntity")).isNotNull()
            assertThat(meta.getEntity("TestEntity2")).isNotNull()
            assertThat(meta.getEntity("ParentEntity")).isNull()
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
    inner class FieldMetadata_Types {

        val fieldsEntity = meta.getEntity("TestFieldsEntity")!!

        @Test
        fun `ObjectId field returns expected metadata`() {
            assertThat(fieldsEntity.fields["id"]).isInstanceOf(IDField::class.java)
            assertThat(fieldsEntity.fields["id"]!!.jvmType).isEqualTo("org.bson.types.ObjectId")
        }

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(fieldsEntity.fields["int_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["int_field"]!!.jvmType).isEqualTo("int")
        }

        @Test
        fun `Short field returns expected metadata`() {
            assertThat(fieldsEntity.fields["short_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["short_field"]!!.jvmType).isEqualTo("short")
        }

        @Test
        fun `Long field returns expected metadata`() {
            assertThat(fieldsEntity.fields["long_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["long_field"]!!.jvmType).isEqualTo("long")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(fieldsEntity.fields["float_field"]).isInstanceOf(FloatField::class.java)
            assertThat(fieldsEntity.fields["float_field"]!!.jvmType).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(fieldsEntity.fields["double_field"]).isInstanceOf(FloatField::class.java)
            assertThat(fieldsEntity.fields["double_field"]!!.jvmType).isEqualTo("double")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(fieldsEntity.fields["boolean_field"]).isInstanceOf(BooleanField::class.java)
            assertThat(fieldsEntity.fields["boolean_field"]!!.jvmType).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(fieldsEntity.fields["string_field"]).isInstanceOf(TextField::class.java)
            assertThat(fieldsEntity.fields["string_field"]!!.jvmType).isEqualTo("java.lang.String")
        }

        @Test
        fun `LocalDate field returns expected metadata`() {
            assertThat(fieldsEntity.fields["date_field"]).isInstanceOf(DateField::class.java)
            assertThat(fieldsEntity.fields["date_field"]!!.jvmType).isEqualTo("java.time.LocalDate")
        }

        @Test
        fun `LocalTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["time_field"]).isInstanceOf(TimeField::class.java)
            assertThat(fieldsEntity.fields["time_field"]!!.jvmType).isEqualTo("java.time.LocalTime")
        }

        @Test
        fun `LocalDateTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["datetime_field"]).isInstanceOf(DateTimeField::class.java)
            assertThat(fieldsEntity.fields["datetime_field"]!!.jvmType).isEqualTo("java.time.LocalDateTime")
        }

        @Test
        fun `Enum field returns expected metadata`() {
            assertThat(fieldsEntity.fields["enum_field"]).isInstanceOf(EnumField::class.java)
            assertThat(fieldsEntity.fields["enum_field"]!!.jvmType).isEqualTo("org.revcrm.testdb.EnumFieldOptions")
        }
    }

    @Nested
    inner class FieldMetadata_Constraints {

        val constraintsEntity = meta.getEntity("TestConstraintsEntity")!!

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
            assertThat(meta.getEntity("SensitiveEntity")!!.apiEnabled).isFalse()
        }
    }
}
