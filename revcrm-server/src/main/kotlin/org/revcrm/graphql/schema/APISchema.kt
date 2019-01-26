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
        registerResultsMetaType(schema)
        registerMutationResultValidationType(schema, code)
        registerEntityDeleteResultType(schema)
        registerMetadataQueryType(schema, code, queryType)

        /**
         * Register Embedded Types
         */
        val embeddedEntities = meta.getEmbeddedEntities()
        embeddedEntities.forEach { entity ->
            registerEntityObjectType(meta, entity, schema)
            registerEntityInputObjectType(meta, entity, schema)
        }

        /**
         * Register Main Entity Query & Mutation fields
         */
        val entities = meta.getEntities()
        entities.forEach { entity ->

            val resultsTypeName = entity.name + "Results"
            registerEntityObjectType(meta, entity, schema)
            registerEntityResultsType(entity, resultsTypeName, schema)
            registerEntityQueryField(queryType, entity, resultsTypeName, code)

            registerEntityInputObjectType(meta, entity, schema)
            if (!entity.isEmbedded)
                registerEntityReferenceType(meta, entity, schema)
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