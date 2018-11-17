package org.revcrm.graphql

import graphql.schema.GraphQLObjectType
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.data.CRMMetadata
import org.revcrm.data.DBService
import org.revcrm.data.EntityMetadata
import org.revcrm.data.FieldMetadata
import org.revcrm.data.FieldService

class APIServiceTests {

    val meta = CRMMetadata(mapOf(
        "TestFieldsModel" to EntityMetadata(
            name = "TestFieldsModel",
            className = "test.TestFieldsModel",
            fields = mapOf(
                "id" to FieldMetadata(name = "id", jvmType = "int"),
                "name" to FieldMetadata(name = "name", jvmType = "java.lang.String"),
                "length" to FieldMetadata(name = "length", jvmType = "float"),
                "size" to FieldMetadata(name = "size", jvmType = "double"),
                "is_awesome" to FieldMetadata(name = "is_awesome", jvmType = "boolean"),
                "created_date" to FieldMetadata(name = "created_date", jvmType = "java.time.LocalDateTime")
            )
        ),
        "Address" to EntityMetadata(
            name = "Address",
            className = "test.Address",
            fields = mapOf(
                "id" to FieldMetadata(name = "id", jvmType = "int"),
                "address_1" to FieldMetadata(name = "address_1", jvmType = "java.lang.String"),
                "address_2" to FieldMetadata(name = "address_2", jvmType = "java.lang.String")
            )
        )
    ))

    val data = DBService().apply {
        mockkObject(this)
    }
    init {
        every { data.getEntityMetadata() } returns meta
    }

    val schema = APIService(data, FieldService()).apply {
        initialise()
    }

    val queryTypeDef = schema.graphQLSchema.queryType
    val testModelDef = queryTypeDef.getFieldDefinition("TestFieldsModel")
    val addressModelDef = queryTypeDef.getFieldDefinition("Address")

    @Nested
    inner class TopLevelSchema {

        @Test
        fun `registers a query object per entity`() {
            assertThat(queryTypeDef.fieldDefinitions).hasSize(meta.entities.size)
        }

        @Test
        fun `registers a GraphQLObjectType for each entity`() {
            assertThat(testModelDef.type is GraphQLObjectType)
            assertThat(addressModelDef.type is GraphQLObjectType)
        }

        // TODO: Tests for each field type
        // Tests for nullability

    }

}