package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.revcrm.db.DBService
import org.revcrm.graphql.schema.APISchema

class APIService(
    private val dbService: DBService
) {
    lateinit var graphQLSchema: GraphQLSchema
    private lateinit var graphQLExecutor: GraphQL
    private lateinit var schema: APISchema

    fun initialise() {
        var crmMeta = dbService.getEntityMetadata()
        schema = APISchema(crmMeta)
        graphQLSchema = schema.build()
        graphQLExecutor = GraphQL.newGraphQL(graphQLSchema).build()
    }

    fun query(query: String, variables: Map<String, Any>?): ExecutionResult {
        val context = APIContext(
            db = dbService,
            defaultResultsLimit = 20 // TODO: Put this in config
        )
        val execInput = ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .context(context)
            .build()
        return graphQLExecutor.execute(execInput)
    }
}