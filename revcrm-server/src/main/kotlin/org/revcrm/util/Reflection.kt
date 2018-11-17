package org.revcrm.util

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.superclasses

// Find a property in the kotlin class' hierarchy
fun getProperty(klass: KClass<*>, fieldName: String): KProperty1<*, *>? {
    val prop = klass.declaredMemberProperties.find { it.name == fieldName }
    if (prop != null) {
        return prop
    }
    klass.superclasses.forEach { superKlass ->
        val superProp = superKlass.declaredMemberProperties.find { it.name == fieldName }
        if (superProp != null) {
            return superProp
        }
    }
    return null
}
