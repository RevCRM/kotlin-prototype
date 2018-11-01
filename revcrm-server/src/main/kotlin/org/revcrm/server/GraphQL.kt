package org.revcrm.server

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import org.koin.ktor.ext.inject
import org.revcrm.graphql.IRevCRMSchema

@KtorExperimentalLocationsAPI
@Location("/graphql")
data class GraphQLRequest(
    val query: String = "",
    val variables: Map<String, Any> = emptyMap()
)

@KtorExperimentalLocationsAPI
fun Routing.graphQL() {

    val crmSchema: IRevCRMSchema by inject()
    val gson = Gson()

    post<GraphQLRequest> {
        val request = call.receive<GraphQLRequest>()

        val query = request.query
        val variables = request.variables
        println("the graphql query: $query")
        println("the graphql variables: $variables")

        val result = crmSchema.query(query, variables)
        val data = result.getData<Any>()
        val response = mutableMapOf("data" to data)
        if (result.errors.isNotEmpty()) {
            response.put("errors", result.errors)
        }
        call.respondText(gson.toJson(response, Map::class.java), ContentType.Application.Json)
//            GraphQLErrors(e).asMap()
    }

}