package org.revcrm.meta

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun getEntityPropInfo(klass: KClass<*>, propName: String): EntityPropInfo {
    val property = klass.memberProperties.find { it.name == propName }!!
    return EntityPropInfo(klass, property)
}