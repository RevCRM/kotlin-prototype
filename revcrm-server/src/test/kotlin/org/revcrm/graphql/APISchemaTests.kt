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
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.annotations.APIDisabled
import org.revcrm.db.DBService
import org.revcrm.graphql.types.GraphQLDate
import org.revcrm.graphql.types.GraphQLDateTime
import org.revcrm.graphql.types.GraphQLObjectID
import org.revcrm.graphql.types.GraphQLTime
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.EnumFieldOptions
import xyz.morphia.annotations.Id
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@APIDisabled
class SensitiveEntity(
    @Id
    var id: ObjectId,
    var name: String
)

class TestFieldsEntity(
    @Id
    var id_field: ObjectId,
    var int_field: Int,
    var short_field: Short,
    var long_field: Long,
    var float_field: Float,
    var double_field: Double,
    var decimal_field: BigDecimal,
    var boolean_field: Boolean,
    var string_field: String,
    var date_field: LocalDate,
    var time_field: LocalTime,
    var datetime_field: LocalDateTime,
    var enum_field: EnumFieldOptions,
    var related_field: TestConstraintsEntity,
    @APIDisabled
    var api_disabled_field: String
)

class TestConstraintsEntity(
    @Id
    var id: ObjectId,
    var nullable_field: String?,
    var non_nullable_field: String
)

val entityClasses = listOf(
    "org.revcrm.graphql.SensitiveEntity",
    "org.revcrm.graphql.TestFieldsEntity",
    "org.revcrm.graphql.TestConstraintsEntity"
)
val embeddedClasses = listOf<String>()

val enabledEntities = 2
val metadataNodes = 1 // query { Metadata }

class APISchemaTests {

    /**
     * Create mock DBService
     */
    val data = DBService().apply {
        mockkObject(this)
    }
    init {
        every { data.getEntityClassNames() } returns entityClasses
        every { data.getEmbeddedClassNames() } returns embeddedClasses
    }

    /**
     * Instantiate MetadataService and APIService
     */
    val meta = MetadataService(data).apply { initialise() }
    val schema = APIService(data, meta).apply { initialise() }

    val queryType = schema.graphQLSchema.queryType
    val testFieldsEntity = queryType.getFieldDefinition("TestFieldsEntity")
    val testConstraintsEntity = queryType.getFieldDefinition("TestConstraintsEntity")
    val metadataNode = queryType.getFieldDefinition("Metadata")

    @Nested
    inner class TopLevelSchema {

        @Test
        fun `registers a query object per entity`() {
            assertThat(queryType.fieldDefinitions).hasSize(enabledEntities + metadataNodes)
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

        @Test
        fun `registers a Metadata node with an "entities" key`() {
            assertThat(metadataNode.type is GraphQLObjectType)

            val entitiesNode = (metadataNode.type as GraphQLObjectType).getFieldDefinition("entities")
            assertThat((entitiesNode.type as GraphQLList).wrappedType.name).isEqualTo("EntityMetadata")
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
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
        }

        @Test
        fun `Long fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("long_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLInt)
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
        fun `Decimal fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("decimal_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(Scalars.GraphQLBigDecimal)
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
            assertThat(type.wrappedType).isEqualTo(GraphQLDate)
        }

        @Test
        fun `Time fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("time_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(GraphQLTime)
        }

        @Test
        fun `DateTime fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("datetime_field").type as GraphQLNonNull
            assertThat(type.wrappedType).isEqualTo(GraphQLDateTime)
        }

        @Test
        fun `Enum fields are exposed as expected`() {
            val type = testFieldsEntityType.getFieldDefinition("enum_field").type as GraphQLNonNull
            val enum = type.wrappedType as GraphQLEnumType
            assertThat(enum.values[0].name).isEqualTo("OPTION1")
            assertThat(enum.values[1].name).isEqualTo("OPTION2")
        }

        @Test
        fun `Fields with apiEnabled = false are not exposed`() {
            assertThat(testFieldsEntityType.getFieldDefinition("api_disabled_field")).isNull()
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