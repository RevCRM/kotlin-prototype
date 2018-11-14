package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.Scalars
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import graphql.schema.StaticDataFetcher
import org.revcrm.data.DBService
import org.revcrm.data.FieldService

class APIService (
    private val dbService: DBService,
    private val fieldService: FieldService
) {
    lateinit var graphQLSchema: GraphQLSchema
    private lateinit var graphQLExecutor: GraphQL

    fun initialise() {
        val meta = dbService.getEntityMetadata()

        val queryType = newObject()
            .name("Query")

        meta.entities.forEach { entity ->

            val entityType = newObject()
                .name(entity.value.name)

            entity.value.fields.forEach { field ->

                var scalarType = fieldService.getGraphQLScalarTypeForField(field.value)
                if (scalarType == null) {
                    val relatedEntity = meta.getEntityByClassName(field.value.jvmType)
                    if (relatedEntity != null) {
                        // TODO: Return ObjectType of related entity
                        scalarType = Scalars.GraphQLString
                    }
                    else {
                        throw Error("Field type '${field.value.jvmType}' for field '${entity.value.name}.${field.value.name}' has no registered GraphQL Mapping.")
                    }
                }

                entityType.field(
                    GraphQLFieldDefinition.newFieldDefinition()
                        .name(field.value.name)
                        .type(scalarType)
                        .dataFetcher(StaticDataFetcher("hello world!"))
                )
            }

            queryType.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(entity.value.name)
                    .type(entityType)
                    .dataFetcher(StaticDataFetcher("hello world!"))
            )
        }

        graphQLSchema = GraphQLSchema(queryType.build())
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