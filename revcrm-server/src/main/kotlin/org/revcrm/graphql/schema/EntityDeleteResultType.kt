package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference

fun registerEntityDeleteResultType(
    schema: GraphQLSchema.Builder
) {

    schema.additionalType(GraphQLObjectType.newObject()
        .name("DeleteEntityResult")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("result")
                .type(Scalars.GraphQLBoolean)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("validation")
                .type(GraphQLTypeReference("MutationValidationResult"))
        )
        .build())
}