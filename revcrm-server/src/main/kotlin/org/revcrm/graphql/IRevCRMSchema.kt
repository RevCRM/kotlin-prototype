package org.revcrm.graphql

import graphql.ExecutionResult

interface IRevCRMSchema {
    fun initialise()
    fun query(query: String, variables: Map<String, Any>?): ExecutionResult
}