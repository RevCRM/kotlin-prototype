package org.revcrm.meta

import org.revcrm.meta.fields.IField

class Entity(
    val name: String,
    val apiEnabled: Boolean,
    val className: String,
    val fields: Map<String, IField>
)
