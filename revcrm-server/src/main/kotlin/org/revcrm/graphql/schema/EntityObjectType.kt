package org.revcrm.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLOutputType
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.Entity

fun buildEntityObjectType(schema: APISchema, entity: Entity): GraphQLObjectType {

    val entityTypeBuilder = GraphQLObjectType.newObject()
        .name(entity.name)

    entity.fields.forEach { _, field ->

        var fieldType = field.getGraphQLType(schema.meta, entity)

        val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
            .name(field.name)
            .dataFetcher(PropertyDataFetcher.fetching<Any>(field.name))

        if (field.nullable) {
            fieldDef.type(fieldType as GraphQLOutputType)
        } else {
            fieldDef.type(GraphQLNonNull(fieldType))
        }

        entityTypeBuilder.field(fieldDef)
    }

    return entityTypeBuilder.build()
}