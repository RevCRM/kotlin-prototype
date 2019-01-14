package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import org.revcrm.db.EntityValidationData

fun registerMutationResultValidationType(schema: GraphQLSchema.Builder, code: GraphQLCodeRegistry.Builder) {

    val fieldErrorType = GraphQLObjectType.newObject()
        .name("MutationValidationFieldError")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("entity")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("fieldPath")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("code")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("message")
                .type(Scalars.GraphQLString)
        )
        .build()

    val entityErrorType = GraphQLObjectType.newObject()
        .name("MutationValidationEntityError")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("entity")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("entityPath")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("code")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("message")
                .type(Scalars.GraphQLString)
        )
        .build()

    schema.additionalType(
        GraphQLObjectType.newObject()
            .name("MutationValidationResult")
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("hasErrors")
                    .type(Scalars.GraphQLBoolean)
            )
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("fieldErrors")
                    .type(GraphQLList(fieldErrorType))
            )
            .field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name("entityErrors")
                    .type(GraphQLList(entityErrorType))
            )
            .build())

    code
        .dataFetcher(
            FieldCoordinates.coordinates("MutationValidationResult", "hasErrors"),
            PropertyDataFetcher.fetching<Boolean, EntityValidationData> { data -> data.hasErrors() }
        )
}