package org.revcrm.graphql.schema

import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLNonNull
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

        if (!field.apiEnabled) return@forEach

        var fieldType = field.getGraphQLInputType(meta, entity)

        val fieldDef = GraphQLInputObjectField.newInputObjectField()
            .name(field.name)

        code.dataFetcher(
            FieldCoordinates.coordinates(entity.name, field.name),
            PropertyDataFetcher.fetching<Any>(field.name)
        )

        if (field.nullable) {
            fieldDef.type(fieldType as GraphQLInputType)
        } else {
            fieldDef.type(GraphQLNonNull(fieldType))
        }

        entityInputTypeBuilder.field(fieldDef)
    }

    schema.additionalType(entityInputTypeBuilder.build())
}