package org.revcrm.data

data class FieldMetadata (
    val name: String,
    val type: String
)

data class EntityMetadata (
    val name: String,
    val className: String,
    val fields: Array<FieldMetadata>
)

data class CRMMetadata (
    val entities: Map<String, EntityMetadata>
)
