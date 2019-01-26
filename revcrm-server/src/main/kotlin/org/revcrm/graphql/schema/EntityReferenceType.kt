package org.revcrm.graphql.schema

import graphql.schema.GraphQLInputObjectField
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLSchema
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

fun registerEntityReferenceType(
    meta: MetadataService,
    entity: Entity,
    schema: GraphQLSchema.Builder
) {

    val entityInputTypeBuilder = GraphQLInputObjectType.newInputObject()
        .name(entity.name + "Reference")

    if (entity.idField == null)
        throw Error("Cannot register reference type for entity '${entity.name}' because it has no @Id field.")

    val idField = entity.idField!!
    val idFieldType = idField.getGraphQLInputType(meta, entity)

    entityInputTypeBuilder.field(
        GraphQLInputObjectField.newInputObjectField()
            .name(idField.name)
            .type(idFieldType)
            .build())

    schema.additionalType(entityInputTypeBuilder.build())
}