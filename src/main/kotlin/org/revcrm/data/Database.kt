package org.revcrm.data

import dagger.Module
import dagger.Provides
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import javax.inject.Singleton

class Database {
    private var factory: SessionFactory

    init {
        val registry = StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build()
        try {
            factory = MetadataSources(registry).buildMetadata().buildSessionFactory()
        } catch (e: Exception) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry)
            throw e
        }
    }

    fun getSession() {
        factory.openSession()
    }

    fun close() {
        factory.close()
    }
}

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return Database()
    }
}