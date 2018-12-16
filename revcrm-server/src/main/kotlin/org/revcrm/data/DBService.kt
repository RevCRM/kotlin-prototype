package org.revcrm.data

import com.mongodb.MongoClient
import org.revcrm.annotations.APIDisabled
import org.revcrm.util.getProperty
import xyz.morphia.Datastore
import xyz.morphia.Morphia
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

class DBService {
    private val morphia = Morphia()
    private lateinit var datastore: Datastore

    fun initialise(
        dbConfig: Map<String, String>,
        entityList: List<String>
    ) {
        entityList.forEach{ morphia.mapPackage(it) }
        datastore = morphia.createDatastore(MongoClient(), "revcrm_new")
        datastore.ensureIndexes()
    }

    fun <T> withDB(method: (Datastore) -> T): T {
        return method(datastore)
    }

    private fun getFieldMetadata(klass: KClass<*>, propName: String): FieldMetadata {

        // TODO: Property processing should be extensible
        val property = getProperty(klass, propName)
        if (property == null) {
            throw Error("Could not locate property '${klass.simpleName}.${propName}'.")
        }
        val nullable = property.returnType.isMarkedNullable
        val constraints = mutableMapOf<String, String>()
        val field = property.javaField!!
        if (field.isAnnotationPresent(NotEmpty::class.java)) {
            constraints.set("NotEmpty", "true")
        }
        if (field.isAnnotationPresent(NotBlank::class.java)) {
            constraints.set("NotBlank", "true")
        }
        if (field.isAnnotationPresent(Min::class.java)) {
            val min = field.getAnnotation(Min::class.java)
            constraints.set("Min", min.value.toString())
        }
        if (field.isAnnotationPresent(Max::class.java)) {
            val max = field.getAnnotation(Max::class.java)
            constraints.set("Max", max.value.toString())
        }

        val fieldMeta = FieldMetadata(
            name = propName,
            jvmType = property.returnType.toString(),
            jvmSubtype = null,
            nullable = nullable,
            constraints = constraints.toMap()
        )
        return fieldMeta
    }

    fun getEntityMetadata(): CRMMetadata {
        val entities = mutableMapOf<String, EntityMetadata>()
        morphia.mapper.mappedClasses.forEach { mapping ->

            // FIXME
            if (mapping.clazz.name.startsWith("java.")) return@forEach

            val klass = mapping.clazz.kotlin
            val apiEnabled = (klass.findAnnotation<APIDisabled>() == null)

            val fields = mutableMapOf<String, FieldMetadata>()

            // Get ID Field
            val idField = mapping.idField
            val idMeta = getFieldMetadata(klass, idField.name)
            fields.put(idMeta.name, idMeta)

            // Get Other Columns
            mapping.persistenceFields.forEach{ field ->
                val meta = getFieldMetadata(klass, field.javaFieldName)
                fields.put(meta.name, meta)
            }

            entities.put(
                mapping.collectionName,
                EntityMetadata(
                    name = mapping.collectionName,
                    apiEnabled = apiEnabled,
                    className = mapping.clazz.name,
                    fields = fields.toMap()
                )
            )
        }
        return CRMMetadata(
            entities = entities.toMap()
        )
    }
}
