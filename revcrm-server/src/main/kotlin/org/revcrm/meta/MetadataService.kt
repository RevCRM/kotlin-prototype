package org.revcrm.meta

import org.revcrm.annotations.APIDisabled
import org.revcrm.db.DBService
import org.revcrm.meta.fields.Field
import org.revcrm.meta.fields.mapBooleanField
import org.revcrm.meta.fields.mapDateField
import org.revcrm.meta.fields.mapDateTimeField
import org.revcrm.meta.fields.mapDecimalField
import org.revcrm.meta.fields.mapEnumField
import org.revcrm.meta.fields.mapFloatField
import org.revcrm.meta.fields.mapIDField
import org.revcrm.meta.fields.mapIntegerField
import org.revcrm.meta.fields.mapListField
import org.revcrm.meta.fields.mapEmbeddedEntityField
import org.revcrm.meta.fields.mapStringField
import org.revcrm.meta.fields.mapTimeField
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

typealias JvmTypeMapper = (
    (meta: MetadataService, propInfo: EntityPropInfo) -> Field
)

class MetadataService(
    private val db: DBService
) {
    private val jvmTypeMappers = mutableMapOf<String, JvmTypeMapper>()

    private val entities = mutableMapOf<String, Entity>()
    private val embeddedEntities = mutableMapOf<String, Entity>()

    init {
        addJvmTypeMapper("int", ::mapIntegerField)
        addJvmTypeMapper("short", ::mapIntegerField)
        addJvmTypeMapper("long", ::mapIntegerField)
        addJvmTypeMapper("float", ::mapFloatField)
        addJvmTypeMapper("double", ::mapFloatField)
        addJvmTypeMapper("boolean", ::mapBooleanField)
        addJvmTypeMapper("java.math.BigDecimal", ::mapDecimalField)
        addJvmTypeMapper("java.lang.String", ::mapStringField)
        addJvmTypeMapper("java.time.LocalDate", ::mapDateField)
        addJvmTypeMapper("java.time.LocalTime", ::mapTimeField)
        addJvmTypeMapper("java.time.LocalDateTime", ::mapDateTimeField)
        addJvmTypeMapper("org.bson.types.ObjectId", ::mapIDField)
        addJvmTypeMapper("java.util.List", ::mapListField)
    }

    fun initialise() {
        db.getEmbeddedClassNames().forEach { className ->
            val entityMeta = getEntityMetadata(className, true)
            addEntity(entityMeta.name, entityMeta)
        }
        db.getEntityClassNames().forEach { className ->
            val entityMeta = getEntityMetadata(className, false)
            if (entityMeta.idField == null) {
                throw Error("Id field for entity '${entityMeta.name}' is not defined.")
            }
            addEntity(entityMeta.name, entityMeta)
        }
    }

    fun getEntityMetadata(className: String, isEmbedded: Boolean): Entity {
        val klass = Class.forName(className).kotlin
        val apiEnabled = (klass.findAnnotation<APIDisabled>() == null)

        val fields = mutableMapOf<String, Field>()

        // Get Entity Property Metadata
        klass.memberProperties.forEach { property ->
            val meta = getEntityField(klass, property)
            fields.put(meta.name, meta)
        }

        return Entity(
            name = klass.simpleName!!,
            isApiEnabled = apiEnabled,
            className = className,
            fields = fields.toMap(),
            isEmbedded = isEmbedded
        )
    }

    private fun getEntityField(klass: KClass<*>, property: KProperty1<*, *>): Field {
        val propInfo = EntityPropInfo(klass, property)
        val entityClassNames = db.getEntityClassNames()
        if (propInfo.isEnum) {
            // TODO: Make this customisable
            return mapEnumField(this, propInfo)
        } else if (propInfo.isEmbedded && db.classIsEmbeddedEntity(propInfo.jvmType)) {
            // TODO: Make this customisable
            return mapEmbeddedEntityField(this, propInfo)
        } else if (!jvmTypeMappers.containsKey(propInfo.jvmType))
            throw Error("Error mapping property '${propInfo.name}'. No scalar type mapper registered for '${propInfo.jvmType}'." +
                " You must use @Embedded or @Reference for related entity properties.")
        val mapper = jvmTypeMappers.get(propInfo.jvmType)!!
        return mapper(this, propInfo)
    }

    fun addJvmTypeMapper(jvmType: String, mapper: JvmTypeMapper) {
        jvmTypeMappers.put(jvmType, mapper)
    }

    fun addEntity(name: String, entity: Entity) {
        entities.put(name, entity)
    }

    fun getEntities(): List<Entity> {
        return entities.values.filter { !it.isEmbedded }
    }

    fun getEmbeddedEntities(): List<Entity> {
        return entities.values.filter { it.isEmbedded }
    }

    fun getAllEntities(): List<Entity> {
        return entities.values.toList()
    }

    fun getEntity(name: String): Entity? {
        return if (entities.containsKey(name)) entities[name] else null
    }
}
