package org.revcrm.controllers

import org.revcrm.graphql.APIService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest

class GraphQLRequest {
    var query: String? = null
    var operationName: String? = null
    var variables: Map<String, Any>? = null
}

@RestController
class GraphQLController(
    val apiService: APIService
) {

    @PostMapping(
        path = ["/graphql"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
    )
    fun graphQLPost(
        @RequestBody body: GraphQLRequest,
        webRequest: WebRequest
    ): Any {
        var query = body.query
        if (query == null) {
            query = ""
        }
        val result = apiService.query(query, body.variables)
        return result.toSpecification()
    }
}