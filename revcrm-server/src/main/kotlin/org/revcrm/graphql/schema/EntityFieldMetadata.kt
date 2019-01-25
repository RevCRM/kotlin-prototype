package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
import graphql.schema.StaticDataFetcher
import org.revcrm.graphql.fetchers.EntityMetadataFetcher
import org.revcrm.meta.Entity
import org.revcrm.meta.fields.Field

@Suppress("UNUSED_PARAMETER")
fun registerMetadataQueryType(
    schema: GraphQLSchema.Builder,
    code: GraphQLCodeRegistry.Builder,
    queryType: GraphQLObjectType.Builder
) {

    val entityFieldMetaType = GraphQLObjectType.newObject()
        .name("EntityFieldMetadata")
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("name")
            .type(Scalars.GraphQLString))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("label")
            .type(Scalars.GraphQLString))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("type")
            .type(Scalars.GraphQLString))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("nullable")
            .type(Scalars.GraphQLBoolean))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("readonly")
            .type(Scalars.GraphQLBoolean))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("properties")
            .type(ExtendedScalars.Json))
        .field(GraphQLFieldDefinition.newFieldDefinition()
            .name("constraints")
            .type(ExtendedScalars.Json))
        .build()

    val entityMetaType = GraphQLObjectType.newObject()
        .name("EntityMetadata")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("idField")
                .type(Scalars.GraphQLString)
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("fields")
                .type(GraphQLList(entityFieldMetaType))
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("isEmbedded")
                .type(Scalars.GraphQLString)
        )
        .build()
    code
        .dataFetcher(
            FieldCoordinates.coordinates("EntityMetadata", "fields"),
            PropertyDataFetcher.fetching<List<Field>, Entity> { entity ->
                entity.fields.values.toList().filter { it.apiEnabled }
            }
        )
        .dataFetcher(
            FieldCoordinates.coordinates("EntityMetadata", "idField"),
            PropertyDataFetcher.fetching<String, Entity> { entity ->
                entity.idField?.name
            }
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