package org.revcrm.routes

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import org.koin.ktor.ext.inject
import org.revcrm.graphql.APIService

@KtorExperimentalLocationsAPI
@Location("/graphql")
data class GraphQLRequest(
    val query: String = "",
    val variables: Map<String, Any> = emptyMap()
)

@KtorExperimentalLocationsAPI
fun Routing.graphQL() {

    val crmSchema: APIService by inject()
    val gson = Gson()

    authenticate("jwt") {
        post<GraphQLRequest> {
            val request = call.receive<GraphQLRequest>()

            val query = request.query
            val variables = request.variables
            println("the graphql query: $query")
            println("the graphql variables: $variables")

            val result = crmSchema.query(query, variables)
            val resJson = result.toSpecification()
            call.respondText(gson.toJson(resJson, Map::class.java), ContentType.Application.Json)
        }
    }
}