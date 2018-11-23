package org.revcrm.data

class FieldMetadata (
    val name: String,
    val jvmType: String,
    val jvmSubtype: String? = null,
    val nullable: Boolean = false,
    val constraints: Map<String, String> = mutableMapOf()
)

class EntityMetadata (
    val name: String,
    val apiEnabled: Boolean,
    val className: String,
    val fields: Map<String, FieldMetadata>
)

class CRMMetadata (
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
