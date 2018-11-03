package org.revcrm.graphql

import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.data.CRMMetadata
import org.revcrm.data.EntityMetadata
import org.revcrm.data.FieldMetadata
import org.revcrm.data.RevCRMData

class RevCRMSchemaTests {

    val meta = CRMMetadata(mapOf(
        "TestModel" to EntityMetadata(
            name = "TestModel",
            className = "test.TestModel",
            fields = mapOf(
                "id" to FieldMetadata(name = "id", type = "int"),
                "name" to FieldMetadata(name = "name", type = "java.lang.String"),
                "length" to FieldMetadata(name = "length", type = "float")
            )
        ),
        "Address" to EntityMetadata(
            name = "Address",
            className = "test.Address",
            fields = mapOf(
                "id" to FieldMetadata(name = "id", type = "int"),
                "address_1" to FieldMetadata(name = "address_1", type = "java.lang.String"),
                "address_2" to FieldMetadata(name = "address_2", type = "java.lang.String")
            )
        )
    ))

    val data = RevCRMData().apply {
        mockkObject(this)
    }

    init {
        every { data.getEntityMetadata() } returns meta
    }

    val schema = RevCRMSchema(data).apply {
        initialise()
    }

    @Nested
    inner class TopLevelSchema {

        @Test
        fun `it doesnt die horibly`() {
            assertThat(1).isEqualTo(1)
        }

    }

}