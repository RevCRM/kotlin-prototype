package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.Scalars
import graphql.schema.DataFetcher
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLList
import graphql.schema.GraphQLNonNull
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import graphql.schema.PropertyDataFetcher
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

        val metaType = newObject()
            .name("ResultsMeta")
            .field(GraphQLFieldDefinition.newFieldDefinition()
                .name("totalCount")
                .type(Scalars.GraphQLInt)
                .dataFetcher(PropertyDataFetcher.fetching<Any>("totalCount"))
            )
            .build()

        meta.entities.forEach { entity ->

            val entityTypeBuilder = newObject()
                .name(entity.value.name)

            entity.value.fields.forEach { field ->

                var scalarType = fieldService.getGraphQLScalarTypeForField(field.value)
                if (scalarType == null) {
                    val relatedEntity = meta.getEntityByClassName(field.value.jvmType)
                    if (relatedEntity != null) {
                        // TODO: Return ObjectType of related entity
                        scalarType = Scalars.GraphQLInt
                    }
                    else {
                        throw Error("Field type '${field.value.jvmType}' for field '${entity.value.name}.${field.value.name}' has no registered GraphQL Mapping.")
                    }
                }

                val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
                    .name(field.value.name)
                    .dataFetcher(PropertyDataFetcher.fetching<Any>(field.value.name))

                if (field.value.nullable) {
                    fieldDef.type(scalarType)
                }
                else {
                    fieldDef.type(GraphQLNonNull(scalarType))
                }

                entityTypeBuilder.field(fieldDef)
            }
            val entityType = entityTypeBuilder.build()

            val entityDataFetcher = DataFetcher<Map<String, Any>> { environment ->
                val ctx = environment.getContext<Any>()

//                val where = environment.getArgument<String>("where")
                val results: List<Map<String, Any>> = listOf(
                    mapOf(
                        "id" to 1,
                        "name" to "The Thing"
                    )
                )
                mapOf(
                    "results" to results,
                    "meta" to mapOf(
                        "totalCount" to 42
                    )
                )
            }

            val entityResultsType = newObject()
                .name(entity.value.name + "Results")
                .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("results")
                    .type(GraphQLList.list(entityType))
                    .dataFetcher(PropertyDataFetcher.fetching<Any>("results"))
                )
                .field(GraphQLFieldDefinition.newFieldDefinition()
                    .name("meta")
                    .type(metaType)
                    .dataFetcher(PropertyDataFetcher.fetching<Any>("meta"))
                )
                .build()

            queryType.field(
                GraphQLFieldDefinition.newFieldDefinition()
                    .name(entity.value.name)
                    .type(entityResultsType)
                    .argument(GraphQLArgument.newArgument()
                        .name("where")
                        .type(Scalars.GraphQLString)
                        .build())
                    .dataFetcher(entityDataFetcher)
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