package org.revcrm.graphql

import graphql.Scalars
import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
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

    val meta = CRMMetadata(
        mapOf(
            "SensitiveModel" to EntityMetadata(
                name = "SensitiveModel",
                apiEnabled = false,
                className = "test.SensitiveModel",
                fields = mapOf(
                    "name" to FieldMetadata(
                            name = "name", jvmType = "java.lang.String",
                            nullable = false
                    )
                )
            ),
            "TestFieldsModel" to EntityMetadata(
                name = "TestFieldsModel",
                apiEnabled = true,
                className = "test.TestFieldsModel",
                fields = mapOf(
                    "int_field" to FieldMetadata(name = "int_field", jvmType = "int"),
                    "short_field" to FieldMetadata(name = "short_field", jvmType = "short"),
                    "long_field" to FieldMetadata(name = "long_field", jvmType = "long"),
                    "float_field" to FieldMetadata(name = "float_field", jvmType = "float"),
                    "double_field" to FieldMetadata(name = "double_field", jvmType = "double"),
                    "boolean_field" to FieldMetadata(name = "boolean_field", jvmType = "boolean"),
                    "string_field" to FieldMetadata(name = "string_field", jvmType = "java.lang.String"),
                    "date_field" to FieldMetadata(name = "date_field", jvmType = "java.time.LocalDate"),
                    "time_field" to FieldMetadata(name = "time_field", jvmType = "java.time.LocalTime"),
                    "datetime_field" to FieldMetadata(name = "datetime_field", jvmType = "java.time.LocalDateTime"),
                    "time_field" to FieldMetadata(name = "time_field", jvmType = "java.time.LocalTime"),
                    "enum_field" to FieldMetadata(
                        name = "enum_field", jvmType = "org.hibernate.type.EnumType",
                        jvmSubtype = "org.revcrm.data.testmodels.EnumFieldOptions"
                    )
                )
            ),
            "TestConstraintsModel" to EntityMetadata(
                name = "TestConstraintsModel",
                apiEnabled = true,
                className = "test.TestConstraintsModel",
                fields = mapOf(
                    "nullable_field" to FieldMetadata(
                        name = "nullable_field", jvmType = "java.lang.String",
                        nullable = true
                    ),
                    "non_nullable_field" to FieldMetadata(
                        name = "non_nullable_field", jvmType = "java.lang.String",
                        nullable = false
                    )
                )
            )
        )
    )

    val data = DBService().apply {
        mockkObject(this)
    }

    init {
        every { data.getEntityMetadata() } returns meta
    }

    val schema = APIService(data, FieldService()).apply {
        initialise()
    }

    val queryType = schema.graphQLSchema.queryType
    val testFieldsModel = queryType.getFieldDefinition("TestFieldsModel")
    val testConstraintsModel = queryType.getFieldDefinition("TestConstraintsModel")

    @Nested
    inner class TopLevelSchema {

        @Test
        fun `registers a query object per entity`() {
            assertThat(queryType.fieldDefinitions).hasSize(meta.entities.size - 1)
        }

        @Test
        fun `does not register entities with apiEnabled = false`() {
            assertThat(queryType.getFieldDefinition("SensitiveModel")).isNull()
        }

        @Test
        fun `registers a GraphQLObjectType for each entity`() {
            assertThat(testFieldsModel.type is GraphQLObjectType)
            assertThat(testConstraintsModel.type is GraphQLObjectType)
        }

        @Test
        fun `Entity root type contains "results" and "meta" keys`() {
            val resultsType = testFieldsModel.type as GraphQLObjectType
            assertThat(resultsType.name).isEqualTo(testFieldsModel.name + "Results")

            val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
            assertThat(resultsListType.wrappedType.name).isEqualTo(testFieldsModel.name)

            val metaNode = resultsType.getFieldDefinition("meta")
            assertThat(metaNode.type is GraphQLObjectType)
        }
    }

    @Nested
    inner class FieldTypes {

        val resultsType = testFieldsModel.type as GraphQLObjectType
        val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
        val testFieldsModelType = resultsListType.wrappedType as GraphQLObjectType

        @Test
        fun `Int fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("int_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
        }

        @Test
        fun `Short fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("short_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLShort)
        }

        @Test
        fun `Long fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("long_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLLong)
        }

        @Test
        fun `Float fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("float_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLFloat)
        }

        @Test
        fun `Double fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("double_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLFloat)
        }

        @Test
        fun `Boolean fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("boolean_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLBoolean)
        }

        @Test
        fun `String fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("string_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Date fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("date_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Time fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("time_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
        }

        @Test
        fun `DateTime fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("datetime_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Enum fields are exposed as expected`() {
            val type = testFieldsModelType.getFieldDefinition("enum_field").type as GraphQLNonNull
            val enum = type.wrappedType as GraphQLEnumType
            assertThat(enum.values[0].name).isEqualTo("OPTION1")
            assertThat(enum.values[1].name).isEqualTo("OPTION2")
        }
    }

    @Nested
    inner class FieldConstraints {

        val resultsType = testConstraintsModel.type as GraphQLObjectType
        val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
        val testConstraintsModelType = resultsListType.wrappedType as GraphQLObjectType

        @Test
        fun `Nullable fields are exposed as expected`() {
            val type = testConstraintsModelType.getFieldDefinition("nullable_field").type
            assertThat(type).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Non-nullable fields are exposed as expected`() {
            val type = testConstraintsModelType.getFieldDefinition("non_nullable_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }
    }
}