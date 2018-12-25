package org.revcrm.data

import com.mongodb.MongoClient
import org.revcrm.annotations.APIDisabled
import org.revcrm.config.Config
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
    private lateinit var config: Config
    private lateinit var client: MongoClient
    private lateinit var datastore: Datastore

    fun initialise(
        newConfig: Config
    ) {
        config = newConfig
        config.entityPackages.forEach { morphia.mapPackage(it) }
        client = MongoClient(config.dbUrl)
        datastore = morphia.createDatastore(client, config.dbName)
        datastore.ensureIndexes()
    }

    fun <T> withDB(method: (Datastore) -> T): T {
        return method(datastore)
    }

    fun getClient(): MongoClient {
        return client
    }

    private fun getFieldMetadata(klass: KClass<*>, propName: String): FieldMetadata {

        // TODO: Property processing should be extensible
        val property = getProperty(klass, propName)
        if (property == null) {
            throw Error("Could not locate property '${klass.simpleName}.$propName'.")
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

        val jvmType: String
        val jvmSubtype: String?
        if (field.type is Class && field.type.isEnum) {
            jvmType = "enum"
            jvmSubtype = field.type.name
        } else {
            jvmType = field.type.name
            jvmSubtype = null
        }

        val fieldMeta = FieldMetadata(
            name = propName,
            jvmType = jvmType,
            jvmSubtype = jvmSubtype,
            nullable = nullable,
            constraints = constraints.toMap()
        )
        return fieldMeta
    }

    fun getEntityMetadata(): CRMMetadata {
        val entities = mutableMapOf<String, EntityMetadata>()
        morphia.mapper.mappedClasses.forEach { mapping ->

            val classMatch = config.entityPackages.find { pkg ->
                mapping.clazz.packageName == pkg
            }
            if (classMatch == null) return@forEach

            val klass = mapping.clazz.kotlin
            val apiEnabled = (klass.findAnnotation<APIDisabled>() == null)

            val fields = mutableMapOf<String, FieldMetadata>()

            // Get ID Field
            val idField = mapping.idField
            val idMeta = getFieldMetadata(klass, idField.name)
            fields.put(idMeta.name, idMeta)

            // Get Other Columns
            mapping.persistenceFields.forEach { field ->
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
