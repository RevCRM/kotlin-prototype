package org.revcrm.data

data class FieldMetadata (
    val name: String,
    val jvmType: String,
    val jvmSubtype: String? = null
)

data class EntityMetadata (
    val name: String,
    val className: String,
    val fields: Map<String, FieldMetadata>
)

data class CRMMetadata (
    val entities: Map<String, EntityMetadata>
) {
    fun getEntityByClassName(className: String): EntityMetadata? {
        for ((_, entity) in entities) {
            if (entity.className == className) {
                return entity
            }
        }
        return null
    }
}
