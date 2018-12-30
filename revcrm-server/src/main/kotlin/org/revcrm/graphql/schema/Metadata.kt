package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import graphql.schema.StaticDataFetcher
import org.revcrm.graphql.EntityMetadataFetcher

fun registerMetadataQueryType(
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder,
    queryType: GraphQLObjectType.Builder
) {

    val entityMetaType = GraphQLObjectType.newObject()
        .name("EntityMetadata")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(Scalars.GraphQLString)
        )
        .build()
    code.dataFetcher(
        FieldCoordinates.coordinates("EntityMetadata", "name"),
        PropertyDataFetcher.fetching<Any>("name")
    )

    val metadataType = GraphQLObjectType.newObject()
        .name("Metadata")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("entities")
                .type(GraphQLList(entityMetaType))
        )
        .build()
    code.dataFetcher(
        FieldCoordinates.coordinates("Metadata", "entities"),
        EntityMetadataFetcher()
    )

    queryType.field(
        GraphQLFieldDefinition.newFieldDefinition()
            .name("Metadata")
            .type(metadataType)
    )
    code.dataFetcher(
        FieldCoordinates.coordinates("Query", "Metadata"),
        StaticDataFetcher(object {})
    )
}