package org.revcrm.db

import com.mongodb.MongoClient
import org.revcrm.config.Config
import xyz.morphia.AdvancedDatastore
import xyz.morphia.Morphia
import xyz.morphia.mapping.MappedClass

class DBService {
    private val morphia = Morphia()
    private lateinit var config: Config
    private lateinit var client: MongoClient
    private lateinit var datastore: AdvancedDatastore

    fun initialise(
        newConfig: Config
    ) {
        config = newConfig
        config.entityClasses.forEach { morphia.map(Class.forName(it)) }
        config.embeddedClasses.forEach { morphia.map(Class.forName(it)) }

        client = MongoClient(config.dbUrl)

        morphia.mapper.addInterceptor(EntityValidator())
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
            // Return only classes in entity class lists
            val className = mapping.clazz.name
            var match = config.entityClasses.find { it == className } != null
            if (match == false)
                match = config.embeddedClasses.find { it == className } != null
            match
        }
    }

    fun getEntityClassNames(): List<String> {
        return config.entityClasses
    }

    fun getEmbeddedClassNames(): List<String> {
        return config.embeddedClasses
    }
}
