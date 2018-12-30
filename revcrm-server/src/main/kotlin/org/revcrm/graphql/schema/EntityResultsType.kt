package org.revcrm.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import graphql.schema.PropertyDataFetcher
import org.revcrm.meta.Entity

fun buildEntityResultsType(schema: APISchema, entity: Entity): GraphQLObjectType {
    return GraphQLObjectType.newObject()
        .name(entity.name + "Results")
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("results")
                .type(GraphQLList.list(GraphQLTypeReference(entity.name)))
                .dataFetcher(PropertyDataFetcher.fetching<Any>("results"))
        )
        .field(
            GraphQLFieldDefinition.newFieldDefinition()
                .name("meta")
                .type(GraphQLTypeReference("ResultsMeta"))
                .dataFetcher(PropertyDataFetcher.fetching<Any>("meta"))
        )
        .build()
}