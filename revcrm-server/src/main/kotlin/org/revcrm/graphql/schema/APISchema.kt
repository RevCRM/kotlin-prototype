package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.scalars.ExtendedScalars
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
import graphql.schema.GraphQLTypeReference
import org.revcrm.graphql.EntityDataFetcher
import org.revcrm.meta.MetadataService

class APISchema(private val meta: MetadataService) {

    fun build(): GraphQLSchema {

        val schema = GraphQLSchema.newSchema()
        val code = GraphQLCodeRegistry.newCodeRegistry()

        /**
         * Register Common Types
         */
        val queryType = GraphQLObjectType.newObject()
            .name("Query")
        registerResultsMetaType(schema, code)

        /**
         * Register Entity Types
         */
        val entities = meta.getEntities()
        entities.forEach { entity ->

            if (!entity.apiEnabled) return@forEach

            registerEntityObjectType(meta, entity, schema, code)

            val resultsTypeName = registerEntityResultsType(entity, schema, code)

            queryType.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(entity.name)
                    .type(GraphQLTypeReference(resultsTypeName))
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("where")
                            .type(ExtendedScalars.Json)
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("orderBy")
                            .type(GraphQLList(Scalars.GraphQLString))
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("limit")
                            .type(ExtendedScalars.PositiveInt)
                            .build())
                    .argument(
                        GraphQLArgument.newArgument()
                            .name("offset")
                            .type(ExtendedScalars.NonNegativeInt)
                            .build())
            )
            code.dataFetcher(
                FieldCoordinates.coordinates("Query", entity.name),
                EntityDataFetcher(entity)
            )
        }
        schema.query(queryType.build())
        schema.codeRegistry(code.build())

        return schema.build()
    }
}