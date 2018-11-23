package org.revcrm.graphql

import graphql.ExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import graphql.Scalars
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

        meta.entities.forEach { _, entity ->

            if (!entity.apiEnabled) return@forEach

            val entityTypeBuilder = newObject()
                .name(entity.name)

            entity.fields.forEach { _, field ->

                var scalarType = fieldService.getGraphQLScalarTypeForField(field)
                if (scalarType == null) {
                    val relatedEntity = meta.getEntityByClassName(field.jvmType)
                    if (relatedEntity != null) {
                        // TODO: Return ObjectType of related entity
                        scalarType = Scalars.GraphQLInt
                    }
                    else {
                        throw Error("Field type '${field.jvmType}' for field '${entity.name}.${field.name}' has no registered GraphQL Mapping.")
                    }
                }

                val fieldDef = GraphQLFieldDefinition.newFieldDefinition()
                    .name(field.name)
                    .dataFetcher(PropertyDataFetcher.fetching<Any>(field.name))

                if (field.nullable) {
                    fieldDef.type(scalarType)
                }
                else {
                    fieldDef.type(GraphQLNonNull(scalarType))
                }

                entityTypeBuilder.field(fieldDef)
            }
            val entityType = entityTypeBuilder.build()

            val entityResultsType = newObject()
                .name(entity.name + "Results")
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
                    .name(entity.name)
                    .type(entityResultsType)
                    .argument(GraphQLArgument.newArgument()
                        .name("where")
                        .type(Scalars.GraphQLString)
                        .build())
                    .dataFetcher(EntityDataFetcher(entity))
            )
        }

        graphQLSchema = GraphQLSchema(queryType.build())
        graphQLExecutor = GraphQL.newGraphQL(graphQLSchema).build()
    }

    fun query(query: String, variables: Map<String, Any>?): ExecutionResult {
        val context = APIContext(
            db = dbService
        )
        val execInput = ExecutionInput.newExecutionInput()
            .query(query)
            .variables(variables)
            .context(context)
            .build()
        return graphQLExecutor.execute(execInput)
    }

}