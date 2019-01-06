package org.revcrm.db

import com.mongodb.MongoClient
import org.revcrm.config.Config
import xyz.morphia.AdvancedDatastore
import xyz.morphia.Morphia
import xyz.morphia.annotations.Entity
import xyz.morphia.mapping.MappedClass
import kotlin.reflect.full.findAnnotation

class DBService {
    private val morphia = Morphia()
    private lateinit var config: Config
    private lateinit var client: MongoClient
    private lateinit var datastore: AdvancedDatastore

    fun initialise(
        newConfig: Config
    ) {
        config = newConfig
        config.entityPackages.forEach { morphia.mapPackage(it) }
        client = MongoClient(config.dbUrl)
        datastore = morphia.createDatastore(client, config.dbName) as AdvancedDatastore
        datastore.ensureIndexes()
    }

    fun <T> withDB(method: (AdvancedDatastore) -> T): T {
        return method(datastore)
    }

    fun getClient(): MongoClient {
        return client
    }

    fun getEntityMappings(): Collection<MappedClass> {
        return morphia.mapper.mappedClasses.filter { mapping ->
            // TODO: Dont filter these out because some are "Enbedded" entities
            val klass = mapping.clazz.kotlin
            klass.findAnnotation<Entity>() != null
        }
    }
}
