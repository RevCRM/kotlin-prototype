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

class RevCRMData {
    lateinit var metadata: Metadata
    private lateinit var factory: SessionFactory

    fun initialise(
        dbConfig: Map<String, String>,
        entityList: List<String>
    ) {
        val registry = StandardServiceRegistryBuilder()
            .applySetting(Environment.CONNECTION_PROVIDER, "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
            .applySetting(Environment.JDBC_TIME_ZONE, "UTC")
            // this will be used programmatically to create/update the DB
//            .applySetting(Environment.HBM2DDL_AUTO, "update")
            // apply supplied settings
            .applySettings(dbConfig)
            .build()

        val sources = MetadataSources(registry)
        entityList.forEach {
            sources.addAnnotatedClassName(it)
        }

        metadata = sources.getMetadataBuilder()
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

    fun <T>withTransaction(method: (Session) -> T): T
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
        var subType: String? = null
        if (value.typeName == "org.hibernate.type.EnumType") {
            subType = value.typeParameters.getProperty("org.hibernate.type.ParameterType.returnedClass")
        }
        val fieldMeta = FieldMetadata(
            name = col.canonicalName,
            jvmType = value.typeName,
            jvmSubtype = subType
        )
        return fieldMeta
    }

    fun getEntityMetadata(): CRMMetadata {
        val entities = mutableMapOf<String, EntityMetadata>()
        metadata.entityBindings.forEach { binding ->
            val fields = mutableMapOf<String, FieldMetadata>()

            // Get ID Column(s)
            val idColIterator = binding.identifierProperty.columnIterator
            while (idColIterator.hasNext()) {
                val column = idColIterator.next() as Column
                val meta = getFieldMetadata(column)
                fields.put(meta.name, meta)
            }

            // Get Other Columns
            val propertyIterator = binding.propertyIterator
            while (propertyIterator.hasNext()) {
                val property = propertyIterator.next() as Property
                val columnIterator = property.getColumnIterator()
                while (columnIterator.hasNext()) {
                    val column = columnIterator.next() as Column
                    val meta = getFieldMetadata(column)
                    fields.put(meta.name, meta)
                }
            }

            entities.put(
                binding.table.name,
                EntityMetadata(
                    name = binding.table.name,
                    className = binding.className,
                    fields = fields.toMap()
                )
            )
        }
        return CRMMetadata(
            entities = entities.toMap()
        )
    }

}
