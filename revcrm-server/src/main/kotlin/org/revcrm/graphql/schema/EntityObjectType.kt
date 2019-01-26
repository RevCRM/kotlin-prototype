package org.revcrm.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

fun registerEntityObjectType(
    meta: MetadataService,
    entity: Entity,
    schema: GraphQLSchema.Builder
) {

    val entityTypeBuilder = GraphQLObjectType.newObject()
        .name(entity.name)

    entity.fields.forEach { _, field ->

        var fieldType = field.getGraphQLType(meta, entity)

        val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
            .name(field.name)

        if (field.nullable) {
            fieldDef.type(fieldType)
        } else {
            fieldDef.type(GraphQLNonNull(fieldType))
        }

        entityTypeBuilder.field(fieldDef)
    }

    schema.additionalType(entityTypeBuilder.build())
}