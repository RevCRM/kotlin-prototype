package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

fun registerEntityInputObjectType(
    meta: MetadataService,
    entity: Entity,
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder
) {

    val entityInputTypeBuilder = GraphQLInputObjectType.newInputObject()
        .name(entity.name + "Input")

    entity.fields.forEach { _, field ->

        if (!field.apiEnabled || field.readonly)
            return@forEach

        val fieldType = field.getGraphQLInputType(meta, entity)

        val fieldDef = GraphQLInputObjectField.newInputObjectField()
            .name(field.name)

        code.dataFetcher(
            FieldCoordinates.coordinates(entity.name, field.name),
            PropertyDataFetcher.fetching<Any>(field.name)
        )

        // Input type fields should be nullable to allow partial updates
        // FUTURE: it might be better to have separate Create/Update input types?
        fieldDef.type(fieldType as GraphQLInputType)

        entityInputTypeBuilder.field(fieldDef)
    }

    schema.additionalType(entityInputTypeBuilder.build())
}