package org.revcrm.meta

import org.revcrm.meta.fields.Field

class Entity(
    val name: String,
    val apiEnabled: Boolean,
    val className: String,
    val fields: Map<String, Field>
) {
    val idField: Field?
        get() = fields.values.find { it.idField }
}
