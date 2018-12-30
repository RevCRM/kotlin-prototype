package org.revcrm.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.EntityMetadata

fun buildEntityObjectType(schema: APISchema, entity: EntityMetadata): GraphQLObjectType {

    val entityTypeBuilder = GraphQLObjectType.newObject()
        .name(entity.name)

    entity.fields.forEach { _, field ->

        var fieldType = schema.getGraphQLScalarTypeForField(field)
        if (fieldType == null) {
            val relatedEntity = schema.meta.getEntityByClassName(field.jvmType)
            if (relatedEntity != null) {
                fieldType = GraphQLTypeReference(relatedEntity.name)
            } else {
                throw Error("Field type '${field.jvmType}' for field '${entity.name}.${field.name}' has no registered GraphQL Mapping.")
            }
        }

        val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
            .name(field.name)
            .dataFetcher(PropertyDataFetcher.fetching<Any>(field.name))

        if (field.nullable) {
            fieldDef.type(fieldType)
        } else {
            fieldDef.type(GraphQLNonNull(fieldType))
        }

        entityTypeBuilder.field(fieldDef)
    }

    return entityTypeBuilder.build()
}