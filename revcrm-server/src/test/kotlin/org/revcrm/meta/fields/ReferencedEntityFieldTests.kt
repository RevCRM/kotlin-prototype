package org.revcrm.meta.fields

import graphql.schema.GraphQLTypeReference
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.revcrm.annotations.Label
import org.revcrm.db.DBService
import org.revcrm.meta.MetadataService
import org.revcrm.meta.ReferencedEntity
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Reference

class EntityWithReferencedEntityField(
    @Id
    var id: ObjectId,
    @Label("Referenced Entity Field")
    @Reference
    var entity: ReferencedEntity
)

class ReferencedEntityFieldTests {

    /**
     * Create mock DBService
     */
    val data = DBService().apply {
        mockkObject(this)
    }
    init {
        every { data.getEntityClassNames() } returns listOf(
            "org.revcrm.meta.fields.EntityWithReferencedEntityField"
        )
        every { data.getEmbeddedClassNames() } returns listOf(
            "org.revcrm.meta.ReferencedEntity"
        )
        every { data.classIsEntity(any()) } returns true
    }

    val meta = MetadataService(data).apply { initialise() }
    val entity = meta.getEntity("EntityWithReferencedEntityField")!!
    val field = entity.fields["entity"]!!

    @Test
    fun `maps the standard properties`() {
        assertThat(field.name).isEqualTo("entity")
        assertThat(field.label).isEqualTo("Referenced Entity Field")
        assertThat(field.type).isEqualTo("ReferencedEntityField")
        assertThat(field.jvmType).isEqualTo("org.revcrm.meta.ReferencedEntity")
        assertThat(field.nullable).isFalse()
    }

    @Test
    fun `maps field constraints`() {
        assertThat(field.constraints).containsEntry("Entity", "ReferencedEntity")
    }

    @Test
    fun `returns the expected GraphQL Output Type`() {
        val gqlType = field.getGraphQLType(meta, entity)
        assertThat(gqlType).isInstanceOf(GraphQLTypeReference::class.java)
        assertThat((gqlType as GraphQLTypeReference).name).isEqualTo("ReferencedEntity")
    }

    @Test
    fun `returns the expected GraphQL Input Type`() {
        val gqlType = field.getGraphQLInputType(meta, entity)
        assertThat(gqlType).isInstanceOf(GraphQLTypeReference::class.java)
        assertThat((gqlType as GraphQLTypeReference).name).isEqualTo("ReferencedEntityReference")
    }
}