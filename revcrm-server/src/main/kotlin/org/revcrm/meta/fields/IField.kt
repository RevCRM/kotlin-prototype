package org.revcrm.meta.fields

import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.MetadataService

interface IField {
    val name: String
    val label: String
    val apiEnabled: Boolean
    val jvmType: String
    val nullable: Boolean
    val properties: Map<String, String>
    val constraints: Map<String, String>

    fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType
    fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLType =
        getGraphQLType(meta, entity)

    val type: String
        get() = this::class.java.simpleName
}