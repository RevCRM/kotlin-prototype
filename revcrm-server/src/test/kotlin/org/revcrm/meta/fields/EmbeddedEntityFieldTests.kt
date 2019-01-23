package org.revcrm.meta.fields

import graphql.schema.GraphQLTypeReference
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.revcrm.annotations.Label
import org.revcrm.db.DBService
import org.revcrm.meta.EmbeddedEntity
import org.revcrm.meta.MetadataService
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Id

class EntityWithEmbeddedEntityField(
    @Id
    var id: ObjectId,
    @Label("Embedded Entity Field")
    @Embedded
    var entity: EmbeddedEntity
)

class EmbeddedEntityFieldTests {

    /**
     * Create mock DBService
     */
    val data = DBService().apply {
        mockkObject(this)
    }
    init {
        every { data.getEntityClassNames() } returns listOf(
            "org.revcrm.meta.fields.EntityWithEmbeddedEntityField"
        )
        every { data.getEmbeddedClassNames() } returns listOf(
            "org.revcrm.meta.EmbeddedEntity"
        )
        every { data.classIsEmbeddedEntity(any()) } returns true
    }

    val meta = MetadataService(data).apply { initialise() }
    val entity = meta.getEntity("EntityWithEmbeddedEntityField")!!
    val field = entity.fields["entity"]!!

    @Test
    fun `maps the standard properties`() {
        assertThat(field.name).isEqualTo("entity")
        assertThat(field.label).isEqualTo("Embedded Entity Field")
        assertThat(field.type).isEqualTo("EmbeddedEntityField")
        assertThat(field.jvmType).isEqualTo("org.revcrm.meta.EmbeddedEntity")
        assertThat(field.nullable).isFalse()
    }

    @Test
    fun `maps field constraints`() {
        assertThat(field.constraints).containsEntry("Entity", "EmbeddedEntity")
    }

    @Test
    fun `returns the expected GraphQL Output Type`() {
        val gqlType = field.getGraphQLType(meta, entity)
        assertThat(gqlType).isInstanceOf(GraphQLTypeReference::class.java)
        assertThat((gqlType as GraphQLTypeReference).name).isEqualTo("EmbeddedEntity")
    }

    @Test
    fun `returns the expected GraphQL Input Type`() {
        val gqlType = field.getGraphQLInputType(meta, entity)
        assertThat(gqlType).isInstanceOf(GraphQLTypeReference::class.java)
        assertThat((gqlType as GraphQLTypeReference).name).isEqualTo("EmbeddedEntityInput")
    }
}