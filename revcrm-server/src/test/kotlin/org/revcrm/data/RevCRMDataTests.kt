package org.revcrm.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.data.testmodels.TestDB

class RevCRMDataTests {

    val testDB = TestDB.instance
    val metadata = testDB.getEntityMetadata()

    @Nested
    inner class EntityMetadata {

        @Test
        fun `returns the correct entities`() {
            assertThat(metadata).hasSize(2)
            assertThat(metadata.containsKey("TestModel"))
            assertThat(metadata.containsKey("TestModel2"))
        }

        @Test
        fun `entity name matches name`() {
            assertThat(metadata["TestModel"]!!.name).isEqualTo("TestModel")
            assertThat(metadata["TestModel2"]!!.name).isEqualTo("TestModel2")
        }

        @Test
        fun `entity className matches full class name`() {
            assertThat(metadata["TestModel"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestModel")
            assertThat(metadata["TestModel2"]!!.className).isEqualTo("org.revcrm.data.testmodels.TestModel2")
        }

        @Test
        fun `returns BaseModel fields`() {
            assertThat(metadata["TestModel"]!!.fields)
                .containsKey("id")
                .containsKey("created_date")
                .containsKey("updated_date")
        }

        @Test
        fun `returns own fields`() {
            assertThat(metadata["TestModel"]!!.fields)
                .containsKey("name")
                .containsKey("age")
        }

    }

    @Nested
    inner class FieldMetadata_Types {

        @Test
        fun `Int field returns expected metadata`() {
            assertThat(metadata["TestModel"]!!.fields["id"]!!.type).isEqualTo("int")
        }

        @Test
        fun `Float field returns expected metadata`() {
            assertThat(metadata["TestModel"]!!.fields["length"]!!.type).isEqualTo("float")
        }

        @Test
        fun `Double field returns expected metadata`() {
            assertThat(metadata["TestModel2"]!!.fields["size"]!!.type).isEqualTo("double")
        }

        @Test
        fun `Boolean field returns expected metadata`() {
            assertThat(metadata["TestModel"]!!.fields["is_awesome"]!!.type).isEqualTo("boolean")
        }

        @Test
        fun `String field returns expected metadata`() {
            assertThat(metadata["TestModel"]!!.fields["name"]!!.type).isEqualTo("java.lang.String")
        }

        @Test
        fun `Timestamp field returns expected metadata`() {
            assertThat(metadata["TestModel"]!!.fields["created_date"]!!.type).isEqualTo("java.sql.Timestamp")
        }

    }
}
