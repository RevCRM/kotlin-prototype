package org.revcrm.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RevCRMDataTests {

    val data = getDataForEntities(listOf(
        "org.revcrm.data.testmodels.TestModel",
        "org.revcrm.data.testmodels.TestModel2"
    )).apply {
        recreateSchema(this)
    }

    val metadata = data.getEntityMetadata()

    @Nested
    inner class GetEntityMetadata {

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

    }
}
