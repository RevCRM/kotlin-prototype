package org.revcrm.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
import org.revcrm.meta.Entity

fun registerEntityMutationResultType(
    entity: Entity,
    mutationResultName: String,
    schema: GraphQLSchema.Builder
) {

    schema.additionalType(GraphQLObjectType.newObject()
        .name(mutationResultName)
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("result")
                .type(GraphQLTypeReference(entity.name))
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("validation")
                .type(GraphQLTypeReference("MutationValidationResult"))
        )
        .build())
}