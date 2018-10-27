package org.revcrm.data

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.Metadata
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Environment
import org.revcrm.models.RevUser
import org.revcrm.models.RevUserAuth

class RevCRMData : IRevCRMData {
    private var metadata: Metadata
    private var factory: SessionFactory

    init {
        val registry = StandardServiceRegistryBuilder()
            // immutable settings
            .applySetting(Environment.JDBC_TIME_ZONE, "UTC")
            // obviously these will be moved somewhere else soon!...
            .applySetting(Environment.DRIVER, "org.postgresql.Driver")
            .applySetting(Environment.URL, "jdbc:postgresql://localhost:5432/revcrm")
            .applySetting(Environment.USER, "revcrm")
            .applySetting(Environment.PASS, "revcrm")
            .applySetting(Environment.POOL_SIZE, "1")
            // this will be used programmatically to create/update the DB
            .applySetting(Environment.HBM2DDL_AUTO, "update")
            .build()

        metadata = MetadataSources(registry)

            // Classes
            .addAnnotatedClass(RevUser::class.java)
            .addAnnotatedClass(RevUserAuth::class.java)

//            .addAnnotatedClassName("org.hibernate.example.Customer")
            .getMetadataBuilder()
            .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
            .build()

        try {
            factory = metadata.buildSessionFactory()
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
