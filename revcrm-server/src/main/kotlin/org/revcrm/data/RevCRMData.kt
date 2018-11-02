package org.revcrm.data

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.Metadata
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Environment
import org.hibernate.mapping.Column
import org.hibernate.mapping.Property
import org.hibernate.mapping.SimpleValue
import org.revcrm.models.RevUser
import org.revcrm.models.RevUserAuth

interface IRevCRMData {
    fun initialise(dbConfig: MutableMap<String, String>)
    fun <T>withTransaction(method: (Session) -> T): T

    fun getEntityMetadata(): Array<EntityMetadata>
}

class RevCRMData : IRevCRMData {
    private lateinit var metadata: Metadata
    private lateinit var factory: SessionFactory

    override fun initialise(dbConfig: MutableMap<String, String>) {
        val registry = StandardServiceRegistryBuilder()
            .applySetting(Environment.CONNECTION_PROVIDER, "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
            .applySettings(dbConfig)
            // immutable settings
            .applySetting(Environment.JDBC_TIME_ZONE, "UTC")
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

    private fun getFieldMetadata(col: Column): FieldMetadata {
        val value = col.value as SimpleValue
        return FieldMetadata(
            name = col.canonicalName,
            type = value.typeName
        )
    }

    override fun getEntityMetadata(): Array<EntityMetadata> {
        val entities = mutableListOf<EntityMetadata>()
        metadata.entityBindings.forEach { binding ->
            val fields = mutableListOf<FieldMetadata>()

            // Get ID Column(s)
            val idColIterator = binding.identifierProperty.columnIterator
            while (idColIterator.hasNext()) {
                val column = idColIterator.next() as Column
                fields.add(getFieldMetadata((column)))
            }

            // Get Other Columns
            val propertyIterator = binding.propertyIterator
            while (propertyIterator.hasNext()) {
                val property = propertyIterator.next() as Property
                val columnIterator = property.getColumnIterator()
                while (columnIterator.hasNext()) {
                    val column = columnIterator.next() as Column
                    fields.add(getFieldMetadata((column)))
                }
            }

            entities.add(
                EntityMetadata(
                    className = binding.className,
                    tableName = binding.table.name,
                    fields = fields.toTypedArray()
                )
            )
        }
        return entities.toTypedArray()
    }

}
