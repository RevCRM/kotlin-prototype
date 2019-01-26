package org.revcrm.meta

import org.bson.types.ObjectId
import xyz.morphia.annotations.Id
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun getEntityPropInfo(klass: KClass<*>, propName: String): EntityPropInfo {
    val property = klass.memberProperties.find { it.name == propName }!!
    return EntityPropInfo(klass, property)
}

class EmbeddedEntity(
    val value: String
)

class ReferencedEntity(
    @Id
    var id: ObjectId,
    var name: String
)