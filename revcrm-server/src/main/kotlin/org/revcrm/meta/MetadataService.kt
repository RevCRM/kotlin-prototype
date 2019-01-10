package org.revcrm.meta

import org.revcrm.annotations.APIDisabled
import org.revcrm.db.DBService
import org.revcrm.meta.fields.IField
import org.revcrm.meta.fields.mapBooleanField
import org.revcrm.meta.fields.mapDateField
import org.revcrm.meta.fields.mapDateTimeField
import org.revcrm.meta.fields.mapDecimalField
import org.revcrm.meta.fields.mapEnumField
import org.revcrm.meta.fields.mapFloatField
import org.revcrm.meta.fields.mapIDField
import org.revcrm.meta.fields.mapIntegerField
import org.revcrm.meta.fields.mapListField
import org.revcrm.meta.fields.mapRelatedEntityField
import org.revcrm.meta.fields.mapTextField
import org.revcrm.meta.fields.mapTimeField
import xyz.morphia.mapping.MappedClass
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

typealias JvmTypeMapper = (
    (meta: MetadataService, propInfo: EntityPropInfo) -> IField
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
        addJvmTypeMapper("java.lang.String", ::mapTextField)
        addJvmTypeMapper("java.time.LocalDate", ::mapDateField)
        addJvmTypeMapper("java.time.LocalTime", ::mapTimeField)
        addJvmTypeMapper("java.time.LocalDateTime", ::mapDateTimeField)
        addJvmTypeMapper("org.bson.types.ObjectId", ::mapIDField)
        addJvmTypeMapper("java.util.List", ::mapListField)
    }

    fun initialise() {
        val mappedClasses = db.getEntityMappings()
        val entityClassNames = db.getEntityClassNames()
        val embeddedClassNames = db.getEmbeddedClassNames()
        mappedClasses.forEach { mapping ->
            val entityMeta = getEntityMetadata(mapping, entityClassNames)
            if (entityClassNames.find { it == entityMeta.className } != null)
                addEntity(entityMeta.name, entityMeta)
            else if (embeddedClassNames.find { it == entityMeta.className } != null)
                addEmbeddedEntity(entityMeta.name, entityMeta)
        }
    }

    private fun getEntityMetadata(mapping: MappedClass, entityClassNames: List<String>): Entity {
        val klass = mapping.clazz.kotlin
        val apiEnabled = (klass.findAnnotation<APIDisabled>() == null)

        val fields = mutableMapOf<String, IField>()

        // Get ID Field
        val idField = mapping.idField
        if (idField == null) {
            throw Error("Id field for entity '${klass.simpleName}' is not defined.")
        }
        val idMeta = getEntityField(klass, idField.name, entityClassNames)
        fields.put(idMeta.name, idMeta)

        // Get Other Columns
        mapping.persistenceFields.forEach { field ->
            val meta = getEntityField(klass, field.javaFieldName, entityClassNames)
            fields.put(meta.name, meta)
        }

        return Entity(
            name = mapping.collectionName,
            apiEnabled = apiEnabled,
            className = mapping.clazz.name,
            fields = fields.toMap()
        )
    }

    private fun getEntityField(klass: KClass<*>, propName: String, entityClassNames: List<String>): IField {
        val propInfo = EntityPropInfo(klass, propName)
        if (propInfo.isEnum) {
            // TODO: Make this customisable
            return mapEnumField(this, propInfo)
        } else if (entityClassNames.contains(propInfo.jvmType)) {
            // TODO: Make this customisable
            val relClass = Class.forName(propInfo.jvmType)
            return mapRelatedEntityField(this, propInfo, relClass.simpleName)
        } else if (!jvmTypeMappers.containsKey(propInfo.jvmType))
            throw Error("No mapper registered for JVM type '${propInfo.jvmType}'.")
        val mapper = jvmTypeMappers.get(propInfo.jvmType)!!
        return mapper(this, propInfo)
    }

    fun addJvmTypeMapper(jvmType: String, mapper: JvmTypeMapper) {
        jvmTypeMappers.put(jvmType, mapper)
    }

    fun addEntity(name: String, entity: Entity) {
        entities.put(name, entity)
    }

    fun addEmbeddedEntity(name: String, entity: Entity) {
        embeddedEntities.put(name, entity)
    }

    fun getEntities(): List<Entity> {
        return entities.values.toList()
    }

    fun getEntity(name: String): Entity? {
        return if (entities.containsKey(name)) entities[name] else null
    }

    fun getEmbeddedEntities(): List<Entity> {
        return embeddedEntities.values.toList()
    }

    fun getEmbeddedEntity(name: String): Entity? {
        return if (embeddedEntities.containsKey(name)) embeddedEntities[name] else null
    }
}
