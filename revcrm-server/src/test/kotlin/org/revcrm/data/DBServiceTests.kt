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
            assertThat(entities).hasSize(2)
            assertThat(entities.containsKey("TestModel"))
            assertThat(entities.containsKey("TestModel2"))
        }

        @Test
        fun `entity name matches name`() {
            assertThat(entities["TestModel"]!!.name).isEqualTo("TestModel")
            assertThat(entities["TestModel2"]!!.name).isEqualTo("TestModel2")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(entities["TestModel"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestModel")
            assertThat(entities["TestModel2"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestModel2")
        }

        @Test
        fun `returns BaseModel fields`() {
            assertThat(entities["TestModel"]!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(entities["TestModel"]!!.fields)
                .containsKey("name")
                .containsKey("age")
        }

    }

    @Nested
    inner class FieldMetadata_Types {

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["id"]!!.jvmType).isEqualTo("int")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["length"]!!.jvmType).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(entities["TestModel2"]!!.fields["size"]!!.jvmType).isEqualTo("double")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["is_awesome"]!!.jvmType).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["name"]!!.jvmType).isEqualTo("java.lang.String")
        }

        @Test
        fun `Timestamp field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["created_date"]!!.jvmType).isEqualTo("java.sql.Timestamp")
        }

        @Test
        fun `Enum field returns expected metadata`() {
            assertThat(entities["TestModel"]!!.fields["enum_field"]!!.jvmType).isEqualTo("org.hibernate.type.EnumType")
            assertThat(entities["TestModel"]!!.fields["enum_field"]!!.jvmSubtype).isEqualTo("org.revcrm.data.testmodels.EnumFieldOptions")
        }

    }
}
