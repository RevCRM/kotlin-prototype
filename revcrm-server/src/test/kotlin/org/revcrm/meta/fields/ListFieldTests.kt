package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLList
import graphql.schema.GraphQLTypeReference
import io.mockk.mockkClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.annotations.Label
import org.revcrm.meta.EmbeddedEntity
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService
import org.revcrm.meta.getEntityPropInfo

class EntityWithEmbeddedEntityListField(
    @Label("Embedded Entity List")
    val entityList: List<EmbeddedEntity>
)

class EntityWithStringListField(
    @Label("Embedded String List")
    val stringList: List<String>
)

class ListFieldTests {

    val mockEntity = Entity(
        name = "mock", className = "Mock", fields = mapOf(),
        isEmbedded = true
    )

    @Nested
    inner class EmbeddedEntityListField {

        val propInfo = getEntityPropInfo(EntityWithEmbeddedEntityListField::class, "entityList")
        var meta = mockkClass(MetadataService::class)

        val field = mapListField(meta, propInfo)

        @Test
        fun `maps the standard properties`() {
            assertThat(field.name).isEqualTo("entityList")
            assertThat(field.label).isEqualTo("Embedded Entity List")
            assertThat(field.type).isEqualTo("EmbeddedEntityListField")
            assertThat(field.jvmType).isEqualTo("java.util.List")
            assertThat(field.nullable).isFalse()
        }

        @Test
        fun `maps field constraints`() {
            assertThat(field.constraints).containsEntry("Entity", "EmbeddedEntity")
        }

        @Test
        fun `returns the expected GraphQL Type`() {
            val gqlType = field.getGraphQLType(meta, mockEntity)
            assertThat(gqlType).isInstanceOf(GraphQLList::class.java)

            val wrappedType = (gqlType as GraphQLList).wrappedType
            assertThat(wrappedType).isInstanceOf(GraphQLTypeReference::class.java)
            assertThat((wrappedType as GraphQLTypeReference).name).isEqualTo("EmbeddedEntity")
        }
    }

    @Nested
    inner class StringListField {

        val propInfo = getEntityPropInfo(EntityWithStringListField::class, "stringList")
        var meta = mockkClass(MetadataService::class)

        val field = mapListField(meta, propInfo)

        @Test
        fun `maps the standard properties`() {
            assertThat(field.name).isEqualTo("stringList")
            assertThat(field.label).isEqualTo("Embedded String List")
            assertThat(field.type).isEqualTo("StringListField")
            assertThat(field.jvmType).isEqualTo("java.util.List")
            assertThat(field.nullable).isFalse()
        }

        @Test
        fun `returns the expected GraphQL Type`() {
            val gqlType = field.getGraphQLType(meta, mockEntity)
            assertThat(gqlType).isInstanceOf(GraphQLList::class.java)

            val wrappedType = (gqlType as GraphQLList).wrappedType
            assertThat(wrappedType).isEqualTo(Scalars.GraphQLString)
        }
    }
}