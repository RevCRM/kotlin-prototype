package org.revcrm.db

import com.mongodb.MongoClient
import org.revcrm.config.Config
import xyz.morphia.AdvancedDatastore
import xyz.morphia.Morphia

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

        client = MongoClient(config.dbUrl)

        morphia.mapper.options.setStoreEmpties(true)
        morphia.mapper.addInterceptor(EntityValidator(this))
        datastore = morphia.createDatastore(client, config.dbName) as AdvancedDatastore
        datastore.ensureIndexes()
    }

    fun <T> withDB(method: (AdvancedDatastore) -> T): T {
        return method(datastore)
    }

    fun getClient(): MongoClient {
        return client
    }

    fun getEntityClassNames(): List<String> {
        return config.entityClasses
    }

    fun getEmbeddedClassNames(): List<String> {
        return config.embeddedClasses
    }

    fun classIsEntity(className: String): Boolean {
        return config.entityClasses.find { it == className } != null
    }

    fun classIsEmbeddedEntity(className: String): Boolean {
        return config.embeddedClasses.find { it == className } != null
    }
}
