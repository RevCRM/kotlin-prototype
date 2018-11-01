package org.revcrm.data

data class FieldMetadata (
    val name: String,
    val type: String
)

data class EntityMetadata (
    val className: String,
    val tableName: String,
    val fields: Array<FieldMetadata>
)
