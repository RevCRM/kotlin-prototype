package org.revcrm.graphql.schema

import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchema
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
        val mutationType = GraphQLObjectType.newObject()
            .name("Mutation")
        registerResultsMetaType(schema, code)
        registerMutationResultValidationType(schema, code)
        registerMetadataQueryType(schema, code, queryType)

        /**
         * Register Embedded Types
         */
        val embeddedEntities = meta.getEmbeddedEntities()
        embeddedEntities.forEach { entity ->
            registerEntityObjectType(meta, entity, schema, code)
            registerEntityInputObjectType(meta, entity, schema, code)
        }

        /**
         * Register Main Entity Query & Mutation fields
         */
        val entities = meta.getEntities()
        entities.forEach { entity ->

            if (!entity.apiEnabled) return@forEach

            val resultsTypeName = entity.name + "Results"
            registerEntityObjectType(meta, entity, schema, code)
            registerEntityResultsType(entity, resultsTypeName, schema, code)
            registerEntityQueryField(queryType, entity, resultsTypeName, code)

            registerEntityInputObjectType(meta, entity, schema, code)
            registerEntityMutations(mutationType, entity, schema, code)
        }

        /**
         * Construct and return schema
         */
        schema.query(queryType.build())
        schema.mutation(mutationType.build())
        schema.codeRegistry(code.build())
        return schema.build()
    }
}