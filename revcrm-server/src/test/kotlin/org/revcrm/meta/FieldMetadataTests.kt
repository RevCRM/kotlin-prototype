package org.revcrm.meta

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB
import org.assertj.core.api.Assertions.assertThat
import org.revcrm.meta.fields.BooleanField
import org.revcrm.meta.fields.DateField
import org.revcrm.meta.fields.DateTimeField
import org.revcrm.meta.fields.DecimalField
import org.revcrm.meta.fields.EnumField
import org.revcrm.meta.fields.FloatField
import org.revcrm.meta.fields.IDField
import org.revcrm.meta.fields.IntegerField
import org.revcrm.meta.fields.RelatedEntityField
import org.revcrm.meta.fields.TextField
import org.revcrm.meta.fields.TimeField

class FieldMetadataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)

    init {
        meta.initialise()
    }

    @Nested
    inner class FieldMetadata_Types {

        val fieldsEntity = meta.getEntity("TestFieldsEntity")!!

        @Test
        fun `ObjectId field returns expected metadata`() {
            assertThat(fieldsEntity.fields["id"]).isInstanceOf(IDField::class.java)
            assertThat(fieldsEntity.fields["id"]!!.type).isEqualTo("IDField")
            assertThat(fieldsEntity.fields["id"]!!.jvmType).isEqualTo("org.bson.types.ObjectId")
            assertThat(fieldsEntity.fields["id"]!!.idField).isTrue()
            assertThat(fieldsEntity.fields["id"]!!.readonly).isFalse()
        }

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(fieldsEntity.fields["int_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["int_field"]!!.type).isEqualTo("IntegerField")
            assertThat(fieldsEntity.fields["int_field"]!!.jvmType).isEqualTo("int")
            assertThat(fieldsEntity.fields["int_field"]!!.idField).isFalse()
            assertThat(fieldsEntity.fields["int_field"]!!.readonly).isFalse()
        }

        @Test
        fun `Short field returns expected metadata`() {
            assertThat(fieldsEntity.fields["short_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["short_field"]!!.label).isEqualTo("Integer Field")
            assertThat(fieldsEntity.fields["short_field"]!!.type).isEqualTo("IntegerField")
            assertThat(fieldsEntity.fields["short_field"]!!.jvmType).isEqualTo("short")
        }

        @Test
        fun `Long field returns expected metadata`() {
            assertThat(fieldsEntity.fields["long_field"]).isInstanceOf(IntegerField::class.java)
            assertThat(fieldsEntity.fields["long_field"]!!.type).isEqualTo("IntegerField")
            assertThat(fieldsEntity.fields["long_field"]!!.jvmType).isEqualTo("long")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(fieldsEntity.fields["float_field"]).isInstanceOf(FloatField::class.java)
            assertThat(fieldsEntity.fields["float_field"]!!.type).isEqualTo("FloatField")
            assertThat(fieldsEntity.fields["float_field"]!!.jvmType).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(fieldsEntity.fields["double_field"]).isInstanceOf(FloatField::class.java)
            assertThat(fieldsEntity.fields["double_field"]!!.type).isEqualTo("FloatField")
            assertThat(fieldsEntity.fields["double_field"]!!.jvmType).isEqualTo("double")
        }

        @Test
        fun `BigDecimal field returns expected metadata`() {
            assertThat(fieldsEntity.fields["decimal_field"]).isInstanceOf(DecimalField::class.java)
            assertThat(fieldsEntity.fields["decimal_field"]!!.type).isEqualTo("DecimalField")
            assertThat(fieldsEntity.fields["decimal_field"]!!.jvmType).isEqualTo("java.math.BigDecimal")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(fieldsEntity.fields["boolean_field"]).isInstanceOf(BooleanField::class.java)
            assertThat(fieldsEntity.fields["boolean_field"]!!.type).isEqualTo("BooleanField")
            assertThat(fieldsEntity.fields["boolean_field"]!!.jvmType).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(fieldsEntity.fields["string_field"]).isInstanceOf(TextField::class.java)
            assertThat(fieldsEntity.fields["string_field"]!!.label).isEqualTo("Text Field")
            assertThat(fieldsEntity.fields["string_field"]!!.type).isEqualTo("TextField")
            assertThat(fieldsEntity.fields["string_field"]!!.jvmType).isEqualTo("java.lang.String")
        }

        @Test
        fun `LocalDate field returns expected metadata`() {
            assertThat(fieldsEntity.fields["date_field"]).isInstanceOf(DateField::class.java)
            assertThat(fieldsEntity.fields["date_field"]!!.type).isEqualTo("DateField")
            assertThat(fieldsEntity.fields["date_field"]!!.jvmType).isEqualTo("java.time.LocalDate")
        }

        @Test
        fun `LocalTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["time_field"]).isInstanceOf(TimeField::class.java)
            assertThat(fieldsEntity.fields["time_field"]!!.type).isEqualTo("TimeField")
            assertThat(fieldsEntity.fields["time_field"]!!.jvmType).isEqualTo("java.time.LocalTime")
        }

        @Test
        fun `LocalDateTime field returns expected metadata`() {
            assertThat(fieldsEntity.fields["datetime_field"]).isInstanceOf(DateTimeField::class.java)
            assertThat(fieldsEntity.fields["datetime_field"]!!.type).isEqualTo("DateTimeField")
            assertThat(fieldsEntity.fields["datetime_field"]!!.jvmType).isEqualTo("java.time.LocalDateTime")
        }

        @Test
        fun `Enum field returns expected metadata`() {
            assertThat(fieldsEntity.fields["enum_field"]).isInstanceOf(EnumField::class.java)
            assertThat(fieldsEntity.fields["enum_field"]!!.type).isEqualTo("EnumField")
            assertThat(fieldsEntity.fields["enum_field"]!!.jvmType).isEqualTo("org.revcrm.testdb.EnumFieldOptions")
        }

        @Test
        fun `Related field returns expected metadata`() {
            assertThat(fieldsEntity.fields["related_field"]).isInstanceOf(RelatedEntityField::class.java)
            assertThat(fieldsEntity.fields["related_field"]!!.type).isEqualTo("RelatedEntityField")
            assertThat(fieldsEntity.fields["related_field"]!!.jvmType).isEqualTo("org.revcrm.testdb.TestEntity2")
        }

        @Test
        fun `Readonly field returns expected metadata`() {
            assertThat(fieldsEntity.fields["readonly_field"]).isInstanceOf(TextField::class.java)
            assertThat(fieldsEntity.fields["readonly_field"]!!.type).isEqualTo("TextField")
            assertThat(fieldsEntity.fields["readonly_field"]!!.jvmType).isEqualTo("java.lang.String")
            assertThat(fieldsEntity.fields["readonly_field"]!!.readonly).isTrue()
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
        fun `NotBlank field returns expected metadata`() {
            assertThat(constraintsEntity.fields["textField"]!!.constraints).containsEntry("NotBlank", "true")
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
}
