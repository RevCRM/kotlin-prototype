package org.revcrm.db

import com.mongodb.DBObject
import org.revcrm.annotations.Stored
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.mapping.Mapper
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class EntityStoredPropertyPersister : AbstractEntityInterceptor() {

    override fun prePersist(ent: Any, dbObj: DBObject, mapper: Mapper) {

        // Scan for members tagged with @Stored that do NOT have a backing field
        // (these members are not automatically persisted by morpia)

        val clazz = ent.javaClass.kotlin
        clazz.memberProperties.forEach { prop ->
            if (
                prop.javaField == null && prop.getter != null &&
                prop.findAnnotation<Stored>() != null
            ) {
                val value = prop.get(ent)
                dbObj.put(prop.name, value)
            }
        }
    }
}
