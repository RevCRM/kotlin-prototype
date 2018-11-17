package org.revcrm.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.data.testmodels.TestDB

class DBServiceTests {

    val testDB = TestDB.instance
    val entities = testDB.getEntityMetadata().entities

    @Nested
    inner class EntityMetadata {

        @Test
        fun `returns the correct entities`() {
            assertThat(entities.containsKey("TestFieldsModel"))
            assertThat(entities.containsKey("TestModel2"))
        }

        @Test
        fun `entity name matches name`() {
            assertThat(entities["TestFieldsModel"]!!.name).isEqualTo("TestFieldsModel")
            assertThat(entities["TestModel2"]!!.name).isEqualTo("TestModel2")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(entities["TestFieldsModel"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestFieldsModel")
            assertThat(entities["TestModel2"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestModel2")
        }

        @Test
        fun `returns BaseModel fields`() {
            assertThat(entities["TestFieldsModel"]!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(entities["TestFieldsModel"]!!.fields)
                .containsKey("string_field")
                .containsKey("int_field")
        }

    }

    @Nested
    inner class FieldMetadata_Types {

        val fieldsModel = entities["TestFieldsModel"]!!

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(fieldsModel.fields["id"]!!.jvmType).isEqualTo("int")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(fieldsModel.fields["float_field"]!!.jvmType).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(fieldsModel.fields["double_field"]!!.jvmType).isEqualTo("double")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(fieldsModel.fields["boolean_field"]!!.jvmType).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(fieldsModel.fields["string_field"]!!.jvmType).isEqualTo("java.lang.String")
        }

        @Test
        fun `LocalDate field returns expected metadata`() {
            assertThat(fieldsModel.fields["date_field"]!!.jvmType).isEqualTo("java.time.LocalDate")
        }

        @Test
        fun `LocalTime field returns expected metadata`() {
            assertThat(fieldsModel.fields["time_field"]!!.jvmType).isEqualTo("java.time.LocalTime")
        }

        @Test
        fun `LocalDateTime field returns expected metadata`() {
            assertThat(fieldsModel.fields["datetime_field"]!!.jvmType).isEqualTo("java.time.LocalDateTime")
        }

        @Test
        fun `Enum field returns expected metadata`() {
            assertThat(fieldsModel.fields["enum_field"]!!.jvmType).isEqualTo("org.hibernate.type.EnumType")
            assertThat(fieldsModel.fields["enum_field"]!!.jvmSubtype).isEqualTo("org.revcrm.data.testmodels.EnumFieldOptions")
        }
    }

    @Nested
    inner class FieldMetadata_Constraints {

        val constraintsModel = entities["TestConstraintsModel"]!!

        @Test
        fun `Non-nullable field returns expected metadata`() {
            assertThat(constraintsModel.fields["non_nullable_field"]!!.nullable).isEqualTo(false)
        }

        @Test
        fun `Nullable field returns expected metadata`() {
            assertThat(constraintsModel.fields["nullable_field"]!!.nullable).isEqualTo(true)
        }

        @Test
        fun `NotEmpty field returns expected metadata`() {
            assertThat(constraintsModel.fields["notempty_field"]!!.constraints).containsEntry("NotEmpty", "true")
        }

        @Test
        fun `NotBlank field returns expected metadata`() {
            assertThat(constraintsModel.fields["notblank_field"]!!.constraints).containsEntry("NotBlank", "true")
        }

        @Test
        fun `Min field returns expected metadata`() {
            assertThat(constraintsModel.fields["min_field"]!!.constraints).containsEntry("Min", "10")
        }

        @Test
        fun `Max field returns expected metadata`() {
            assertThat(constraintsModel.fields["max_field"]!!.constraints).containsEntry("Max", "100")
        }
    }
}
