package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.revcrm.data.IRevCRMData

interface IRevCRMSchema {
    fun initialise()
    fun query(query: String, variables: Map<String, Any>?): ExecutionResult
}

class RevCRMSchema (private val data: IRevCRMData) : IRevCRMSchema {
    private lateinit var graphQLSchema: GraphQLSchema
    private lateinit var graphQLExecutor: GraphQL

    override fun initialise() {
        val meta = data.getEntityMetadata()
        println("found ${meta.entities.size} entities!")
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

    override fun query(query: String, variables: Map<String, Any>?): ExecutionResult {
        val execInput = ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .build()
        return graphQLExecutor.execute(execInput)
    }

}