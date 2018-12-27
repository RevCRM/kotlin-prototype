package org.revcrm.graphql

class EntitySearchResults<T>(
    val results: List<T>,
    val meta: EntitySearchMeta
)

class EntitySearchMeta(
    val totalCount: Long
)