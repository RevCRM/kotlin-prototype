package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLOutputType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

fun registerEntityObjectType(
    meta: MetadataService,
    entity: Entity,
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder
) {

    val entityTypeBuilder = GraphQLObjectType.newObject()
        .name(entity.name)

    entity.fields.forEach { _, field ->

        if (!field.apiEnabled) return@forEach

        var fieldType = field.getGraphQLType(meta, entity)

        val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
            .name(field.name)

        code.dataFetcher(
            FieldCoordinates.coordinates(entity.name, field.name),
            PropertyDataFetcher.fetching<Any>(field.name)
        )

        if (field.nullable) {
            fieldDef.type(fieldType as GraphQLOutputType)
        } else {
            fieldDef.type(GraphQLNonNull(fieldType))
        }

        entityTypeBuilder.field(fieldDef)
    }

    schema.additionalType(entityTypeBuilder.build())
}