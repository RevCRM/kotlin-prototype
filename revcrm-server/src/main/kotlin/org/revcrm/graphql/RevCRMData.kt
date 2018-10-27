package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser

class RevCRMData {
    private val graphQLSchema: GraphQLSchema
    private val graphQLExecutor: GraphQL

    init {
        val schema = "type Query{hello: String}"
        val schemaParser = SchemaParser()
        val typeDefinitionRegistry = schemaParser.parse(schema)
        val runtimeWiring = newRuntimeWiring()
            .type("Query") { builder -> builder.dataFetcher("hello", StaticDataFetcher("world")) }
            .build()
        val schemaGenerator = SchemaGenerator()
        graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
        graphQLExecutor = GraphQL.newGraphQL(graphQLSchema).build()
    }

    fun query(query: String, variables: Map<String, Any>?): ExecutionResult {
        val execInput = ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .build()
        return graphQLExecutor.execute(execInput)
    }

}