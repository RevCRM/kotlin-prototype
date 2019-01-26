package org.revcrm.db

import com.mongodb.MongoClient
import org.revcrm.config.AppConfig
import org.revcrm.config.DBConfig
import xyz.morphia.AdvancedDatastore
import xyz.morphia.Morphia

class DBService {
    private val morphia = Morphia()
    private lateinit var config: DBConfig
    private lateinit var client: MongoClient
    private lateinit var datastore: AdvancedDatastore

    val entityContextFIXME = EntityContext(
        config = AppConfig()
    )

    fun initialise(
        newConfig: DBConfig
    ) {
        config = newConfig
        config.entityClasses.forEach { morphia.map(Class.forName(it)) }

        client = MongoClient(config.dbUrl)

        // for multi-tenant we'll need a seperate Mapper + Datastore for each client
        // Mappers contain an instance cache, and datastores obviously have the reference to the appropriate database
        morphia.mapper.options.setStoreEmpties(true)

        // inject context - TODO: sort out multi-user and multi-tenant context
        morphia.mapper.options.setObjectFactory(EntityCreator(entityContextFIXME))

        // register validator and @Stored property interceptor
        morphia.mapper.addInterceptor(EntityValidator(this))
        morphia.mapper.addInterceptor(EntityStoredPropertyPersister())

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
