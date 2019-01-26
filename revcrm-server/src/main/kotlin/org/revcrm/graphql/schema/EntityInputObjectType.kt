package org.revcrm.graphql.schema

import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLSchema
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

fun registerEntityInputObjectType(
    meta: MetadataService,
    entity: Entity,
    schema: GraphQLSchema.Builder
) {

    val entityInputTypeBuilder = GraphQLInputObjectType.newInputObject()
        .name(entity.name + "Input")

    entity.fields.forEach { _, field ->

        if (field.readonly) return@forEach

        val fieldType = field.getGraphQLInputType(meta, entity)

        val fieldDef = GraphQLInputObjectField.newInputObjectField()
            .name(field.name)

        // Input type fields should be nullable to allow partial updates
        // FUTURE: it might be better to have separate Create/Update input types?
        fieldDef.type(fieldType)

        entityInputTypeBuilder.field(fieldDef)
    }

    schema.additionalType(entityInputTypeBuilder.build())
}