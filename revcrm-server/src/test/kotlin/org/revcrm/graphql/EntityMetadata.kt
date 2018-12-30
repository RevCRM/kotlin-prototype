package org.revcrm.graphql

import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.meta.CRMMetadata
import org.revcrm.db.DBService
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityField

class EntityMetadata {

    val meta = CRMMetadata(
        mapOf(
            "SensitiveEntity" to Entity(
                name = "SensitiveEntity",
                apiEnabled = false,
                className = "test.SensitiveEntity",
                fields = mapOf(
                    "name" to EntityField(
                        name = "name", jvmType = "java.lang.String",
                        nullable = false
                    )
                )
            ),
            "TestFieldsEntity" to Entity(
                name = "TestFieldsEntity",
                apiEnabled = true,
                className = "test.TestFieldsEntity",
                fields = mapOf(
                    "id_field" to EntityField(name = "id_field", jvmType = "org.bson.types.ObjectId"),
                    "int_field" to EntityField(name = "int_field", jvmType = "int"),
                    "short_field" to EntityField(name = "short_field", jvmType = "short"),
                    "long_field" to EntityField(name = "long_field", jvmType = "long"),
                    "float_field" to EntityField(name = "float_field", jvmType = "float"),
                    "double_field" to EntityField(name = "double_field", jvmType = "double"),
                    "boolean_field" to EntityField(name = "boolean_field", jvmType = "boolean"),
                    "string_field" to EntityField(
                        name = "string_field",
                        jvmType = "java.lang.String"
                    ),
                    "date_field" to EntityField(name = "date_field", jvmType = "java.time.LocalDate"),
                    "time_field" to EntityField(name = "time_field", jvmType = "java.time.LocalTime"),
                    "datetime_field" to EntityField(
                        name = "datetime_field",
                        jvmType = "java.time.OffsetDateTime"
                    ),
                    "time_field" to EntityField(name = "time_field", jvmType = "java.time.LocalTime"),
                    "enum_field" to EntityField(
                        name = "enum_field", jvmType = "enum",
                        jvmSubtype = "org.revcrm.testdb.EnumFieldOptions"
                    )
                )
            ),
            "TestConstraintsEntity" to Entity(
                name = "TestConstraintsEntity",
                apiEnabled = true,
                className = "test.TestConstraintsEntity",
                fields = mapOf(
                    "nullable_field" to EntityField(
                        name = "nullable_field", jvmType = "java.lang.String",
                        nullable = true
                    ),
                    "non_nullable_field" to EntityField(
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

    val schema = APIService(data).apply {
        initialise()
    }

    val queryType = schema.graphQLSchema.queryType
    val testFieldsEntity = queryType.getFieldDefinition("TestFieldsEntity")
    val testConstraintsEntity = queryType.getFieldDefinition("TestConstraintsEntity")

    @Nested
    inner class TopLevelSchema {

        @Test
        fun `registers a query object per entity`() {
            assertThat(queryType.fieldDefinitions).hasSize(meta.entities.size - 1)
        }

        @Test
        fun `does not register entities with apiEnabled = false`() {
            assertThat(queryType.getFieldDefinition("SensitiveEntity")).isNull()
        }

        @Test
        fun `registers a GraphQLObjectType for each entity`() {
            assertThat(testFieldsEntity.type is GraphQLObjectType)
            assertThat(testConstraintsEntity.type is GraphQLObjectType)
        }

        @Test
        fun `Entity root type has "orderBy" argument`() {
            assertThat(testFieldsEntity.arguments).anyMatch { arg -> arg.name == "orderBy" }

            val arg = testFieldsEntity.arguments.find { arg -> arg.name == "orderBy" }!!
            assertThat(arg.type).isInstanceOf(GraphQLList::class.java)
            assertThat((arg.type as GraphQLList).wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Entity root type has "where" argument`() {
            assertThat(testFieldsEntity.arguments).anyMatch { arg -> arg.name == "where" }

            val arg = testFieldsEntity.arguments.find { arg -> arg.name == "where" }!!
            assertThat(arg.type).isEqualTo(ExtendedScalars.Json)
        }

        @Test
        fun `Entity root type has "limit" argument`() {
            assertThat(testFieldsEntity.arguments).anyMatch { arg -> arg.name == "limit" }
            val arg = testFieldsEntity.arguments.find { arg -> arg.name == "limit" }!!
            assertThat(arg.type).isEqualTo(ExtendedScalars.PositiveInt)
        }

        @Test
        fun `Entity root type has "offset" argument`() {
            assertThat(testFieldsEntity.arguments).anyMatch { arg -> arg.name == "offset" }
            val arg = testFieldsEntity.arguments.find { arg -> arg.name == "offset" }!!
            assertThat(arg.type).isEqualTo(ExtendedScalars.NonNegativeInt)
        }

        @Test
        fun `Entity root type contains "results" and "meta" keys`() {
            val resultsType = testFieldsEntity.type as GraphQLObjectType
            assertThat(resultsType.name).isEqualTo(testFieldsEntity.name + "Results")

            val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
            assertThat(resultsListType.wrappedType.name).isEqualTo(testFieldsEntity.name)

            val metaNode = resultsType.getFieldDefinition("meta")
            assertThat(metaNode.type is GraphQLObjectType)
        }
    }

    @Nested
    inner class FieldTypes {

        val resultsType = testFieldsEntity.type as GraphQLObjectType
        val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
        val testFieldsEntityType = resultsListType.wrappedType as GraphQLObjectType

        @Test
        fun `ObjectId fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("id_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(GraphQLObjectID)
        }

        @Test
        fun `Int fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("int_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
        }

        @Test
        fun `Short fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("short_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLShort)
        }

        @Test
        fun `Long fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("long_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLLong)
        }

        @Test
        fun `Float fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("float_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLFloat)
        }

        @Test
        fun `Double fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("double_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLFloat)
        }

        @Test
        fun `Boolean fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("boolean_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLBoolean)
        }

        @Test
        fun `String fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("string_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Date fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("date_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Time fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("time_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
        }

        @Test
        fun `DateTime fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("datetime_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Enum fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("enum_field").type as GraphQLNonNull
            val enum = type.wrappedType as GraphQLEnumType
            assertThat(enum.values[0].name).isEqualTo("OPTION1")
            assertThat(enum.values[1].name).isEqualTo("OPTION2")
        }
    }

    @Nested
    inner class FieldConstraints {

        val resultsType = testConstraintsEntity.type as GraphQLObjectType
        val resultsListType = resultsType.getFieldDefinition("results").type as GraphQLList
        val testConstraintsEntityType = resultsListType.wrappedType as GraphQLObjectType

        @Test
        fun `Nullable fields are exposed as expected`() {
            val type = testConstraintsEntityType.getFieldDefinition("nullable_field").type
            assertThat(type).isEqualTo(Scalars.GraphQLString)
        }

        @Test
        fun `Non-nullable fields are exposed as expected`() {
            val type = testConstraintsEntityType.getFieldDefinition("non_nullable_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLString)
        }
    }
}