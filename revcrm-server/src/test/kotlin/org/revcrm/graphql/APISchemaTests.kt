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
import org.revcrm.db.DBService
import org.revcrm.graphql.types.GraphQLDate
import org.revcrm.graphql.types.GraphQLDateTime
import org.revcrm.graphql.types.GraphQLObjectID
import org.revcrm.graphql.types.GraphQLTime
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService
import org.revcrm.meta.fields.BooleanField
import org.revcrm.meta.fields.DateField
import org.revcrm.meta.fields.DateTimeField
import org.revcrm.meta.fields.DecimalField
import org.revcrm.meta.fields.EnumField
import org.revcrm.meta.fields.FloatField
import org.revcrm.meta.fields.IDField
import org.revcrm.meta.fields.IField
import org.revcrm.meta.fields.IntegerField
import org.revcrm.meta.fields.RelatedEntityField
import org.revcrm.meta.fields.TextField
import org.revcrm.meta.fields.TimeField

class APISchemaTests {

    /**
     * Create mock metadata
     */
    val entities = listOf<Entity>(
        Entity(
            name = "SensitiveEntity",
            idField = "id",
            apiEnabled = false,
            className = "test.SensitiveEntity",
            fields = mapOf(
                "name" to TextField(
                    name = "name", label = "Name", apiEnabled = true,
                    jvmType = "java.lang.String",
                    nullable = false, properties = mapOf(),
                    constraints = mapOf()
                )
            )
        ),
        Entity(
            name = "TestFieldsEntity",
            idField = "id",
            apiEnabled = true,
            className = "test.TestFieldsEntity",
            fields = mapOf<String, IField>(
                "id_field" to IDField(name = "id_field", label = "ID", apiEnabled = true,
                    jvmType = "org.bson.types.ObjectId", nullable = false),
                "int_field" to IntegerField(name = "int_field", label = "Integer Field", apiEnabled = true,
                    jvmType = "int", nullable = false, properties = mapOf(), constraints = mapOf()),
                "short_field" to IntegerField(name = "short_field", label = "Short Field", apiEnabled = true,
                    jvmType = "short", nullable = false, properties = mapOf(), constraints = mapOf()),
                "long_field" to IntegerField(name = "long_field", label = "Long Field", apiEnabled = true,
                    jvmType = "long", nullable = false, properties = mapOf(), constraints = mapOf()),
                "float_field" to FloatField(name = "float_field", label = "Float Field", apiEnabled = true,
                    jvmType = "float", nullable = false, properties = mapOf(), constraints = mapOf()),
                "double_field" to FloatField(name = "double_field", label = "Double Field", apiEnabled = true,
                    jvmType = "double", nullable = false, properties = mapOf(), constraints = mapOf()),
                "decimal_field" to DecimalField(name = "decimal_field", label = "Decimal Field", apiEnabled = true,
                    jvmType = "java.math.BigDecimal", nullable = false, properties = mapOf(), constraints = mapOf()),
                "boolean_field" to BooleanField(name = "boolean_field", label = "Boolean Field", apiEnabled = true,
                    jvmType = "boolean", nullable = false),
                "string_field" to TextField(name = "string_field", label = "String Field", apiEnabled = true,
                    jvmType = "java.lang.String", nullable = false, properties = mapOf(), constraints = mapOf()),
                "date_field" to DateField(name = "date_field", label = "Date Field", apiEnabled = true,
                    jvmType = "java.time.LocalDate", nullable = false, properties = mapOf(), constraints = mapOf()),
                "time_field" to TimeField(name = "time_field", label = "Time Field", apiEnabled = true,
                    jvmType = "java.time.LocalTime", nullable = false, properties = mapOf(), constraints = mapOf()),
                "datetime_field" to DateTimeField(name = "datetime_field", label = "DateTime Field", apiEnabled = true,
                    jvmType = "java.time.LocalDateTime", nullable = false, properties = mapOf(), constraints = mapOf()),
                "enum_field" to EnumField(name = "enum_field", label = "Enum Field", apiEnabled = true,
                    jvmType = "org.revcrm.testdb.EnumFieldOptions", nullable = false),
                "related_field" to RelatedEntityField(name = "related_field", label = "RelatedEntity Field", apiEnabled = true,
                    jvmType = "org.revcrm.testdb.TestConstraintsEntity", nullable = false, relatedEntity = "TestConstraintsEntity"),
                "api_disabled_field" to TextField(name = "api_disabled_field", label = "Disabled Field", apiEnabled = false,
                    jvmType = "java.lang.String", nullable = false, properties = mapOf(), constraints = mapOf())
            )
        ),
        Entity(
            name = "TestConstraintsEntity",
            idField = "id",
            apiEnabled = true,
            className = "test.TestConstraintsEntity",
            fields = mapOf<String, IField>(
                "nullable_field" to TextField(name = "nullable_field", label = "Nullable Text Field", apiEnabled = true,
                    jvmType = "java.lang.String", nullable = true, properties = mapOf(), constraints = mapOf()
                ),
                "non_nullable_field" to TextField(name = "non_nullable_field", label = "Non-nullable Text Field", apiEnabled = true,
                    jvmType = "java.lang.String", nullable = false, properties = mapOf(), constraints = mapOf()
                )
            )
        )
    )
    val enabledEntities = 2
    val metadataNodes = 1 // query { Metadata }

    /**
     * Create mock DBService and MetadataService
     */
    val data = DBService().apply {
        mockkObject(this)
    }

    val meta = MetadataService(data).apply {
        mockkObject(this)
    }
    init {
        every { meta.getEntities() } returns entities
        every { meta.getEmbeddedEntities() } returns listOf()
    }

    /**
     * Instantiate APIService using mock data sources
     */
    val schema = APIService(data, meta).apply {
        initialise()
    }

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