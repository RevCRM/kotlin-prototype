package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.revcrm.db.DBService
import org.revcrm.graphql.schema.APISchema
import org.revcrm.meta.MetadataService
import org.springframework.stereotype.Service

@Service
class APIService(
    private val dbService: DBService,
    private val meta: MetadataService
) {

    private val graphQLSchema: GraphQLSchema
    private val graphQLExecutor: GraphQL
    private val schema: APISchema

    init {
        schema = APISchema(meta)
        graphQLSchema = schema.build()
        graphQLExecutor = GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildQuery(query: String, variables: Map<String, Any>?): ExecutionInput {
        val context = APIContext(
            db = dbService,
            meta = meta,
            defaultResultsLimit = 20 // TODO: Put this in config
        )

        return ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .context(context)
            .build()
    }

    fun query(query: String, variables: Map<String, Any>?): ExecutionResult {
        return graphQLExecutor.execute(
            buildQuery(
                query, variables
            )
        )
    }
}