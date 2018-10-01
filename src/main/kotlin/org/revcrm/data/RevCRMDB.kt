package org.revcrm.data

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

interface IRevCRMDB {
    fun <T>withTransaction(method: (Session) -> T): T
    fun close()
}

class RevCRMDB : IRevCRMDB {
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

    fun getSession(): Session {
        return factory.openSession()
    }

    override fun <T>withTransaction(method: (Session) -> T): T
        = getSession().use { session ->
            session.beginTransaction()
            try {
                val result = method(session)
                session.getTransaction().commit()
                return result
            }
            catch (e: Exception) {
                session.getTransaction().rollback()
                throw e
            }
            finally {
                session.close()
            }
        }

    override fun close() {
        factory.close()
    }
}
