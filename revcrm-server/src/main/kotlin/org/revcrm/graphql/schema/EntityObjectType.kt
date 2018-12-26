package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.PropertyDataFetcher
import org.revcrm.data.EntityMetadata

fun buildEntityObjectType(schema: APISchema, entity: EntityMetadata): GraphQLObjectType {

    val entityTypeBuilder = GraphQLObjectType.newObject()
        .name(entity.name)

    entity.fields.forEach { _, field ->

        var scalarType = schema.getGraphQLScalarTypeForField(field)
        if (scalarType == null) {
            val relatedEntity = schema.meta.getEntityByClassName(field.jvmType)
            if (relatedEntity != null) {
                // TODO: Return ObjectType of related entity
                scalarType = Scalars.GraphQLInt
            } else {
                throw Error("Field type '${field.jvmType}' for field '${entity.name}.${field.name}' has no registered GraphQL Mapping.")
            }
        }

        val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
            .name(field.name)
            .dataFetcher(PropertyDataFetcher.fetching<Any>(field.name))

        if (field.nullable) {
            fieldDef.type(scalarType)
        } else {
            fieldDef.type(GraphQLNonNull(scalarType))
        }

        entityTypeBuilder.field(fieldDef)
    }

    return entityTypeBuilder.build()
}