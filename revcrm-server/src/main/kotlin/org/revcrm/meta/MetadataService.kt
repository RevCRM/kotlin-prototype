package org.revcrm.meta

import org.revcrm.annotations.APIDisabled
import org.revcrm.db.DBService
import org.revcrm.meta.fields.IField
import org.revcrm.meta.fields.mapBooleanField
import org.revcrm.meta.fields.mapDateField
import org.revcrm.meta.fields.mapDateTimeField
import org.revcrm.meta.fields.mapEnumField
import org.revcrm.meta.fields.mapFloatField
import org.revcrm.meta.fields.mapIDField
import org.revcrm.meta.fields.mapIntegerField
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

    init {
        addJvmTypeMapper("int", ::mapIntegerField)
        addJvmTypeMapper("short", ::mapIntegerField)
        addJvmTypeMapper("long", ::mapIntegerField)
        addJvmTypeMapper("float", ::mapFloatField)
        addJvmTypeMapper("double", ::mapFloatField)
        addJvmTypeMapper("boolean", ::mapBooleanField)
        addJvmTypeMapper("java.lang.String", ::mapTextField)
        addJvmTypeMapper("java.time.LocalDate", ::mapDateField)
        addJvmTypeMapper("java.time.LocalTime", ::mapTimeField)
        addJvmTypeMapper("java.time.OffsetDateTime", ::mapDateTimeField)
        addJvmTypeMapper("org.bson.types.ObjectId", ::mapIDField)

        val mappedClasses = db.getEntityMappings()
        val entityClassNames = mappedClasses.map { it.clazz.name }
        mappedClasses.forEach { mapping ->
            val entityMeta = getEntityMetadata(mapping, entityClassNames)
            addEntity(entityMeta.name, entityMeta)
        }
    }

    fun getEntityMetadata(mapping: MappedClass, entityClassNames: List<String>): Entity {
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

    fun getEntityField(klass: KClass<*>, propName: String, entityClassNames: List<String>): IField {
        val propInfo = EntityPropInfo(klass, propName)
        if (propInfo.field.type.isEnum) {
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

    fun getEntities(): List<Entity> {
        return entities.values.toList()
    }
}
