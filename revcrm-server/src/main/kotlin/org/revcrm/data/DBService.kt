package org.revcrm.data

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.Metadata
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Environment
import org.hibernate.mapping.Property
import org.hibernate.mapping.SimpleValue
import org.revcrm.util.getProperty
import javax.persistence.EntityManager
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import kotlin.reflect.jvm.javaField

class DBService {
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

    fun getEntityManager(session: Session): EntityManager {
        return session.entityManagerFactory.createEntityManager()
    }

    fun <T>withTransaction(method: (EntityManager) -> T): T {
        val session = getSession()
        val entityManager = getEntityManager(session)

        entityManager.transaction.begin()
        val result: T
        try {
            result = method(entityManager)
            entityManager.transaction.commit()
            return result
        }
        catch (e: Exception) {
            entityManager.transaction.rollback()
            throw e
        }
        finally {
            entityManager.close()
        }
    }

    private fun getFieldMetadata(prop: Property): FieldMetadata {
        val value = prop.value as SimpleValue
        var subType: String? = null
        if (value.typeName == "org.hibernate.type.EnumType") {
            subType = value.typeParameters.getProperty("org.hibernate.type.ParameterType.returnedClass")
        }

        // TODO: Property processing should be extensible
        val klass = prop.persistentClass.mappedClass.kotlin
        val property = getProperty(klass, prop.name)
        if (property == null) {
            throw Error("Could not locate property '${klass.simpleName}.${prop.name}'.")
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

        val fieldMeta = FieldMetadata(
            name = prop.name,
            jvmType = value.typeName,
            jvmSubtype = subType,
            nullable = nullable,
            constraints = constraints.toMap()
        )
        return fieldMeta
    }

    fun getEntityMetadata(): CRMMetadata {
        val entities = mutableMapOf<String, EntityMetadata>()
        metadata.entityBindings.forEach { binding ->
            val fields = mutableMapOf<String, FieldMetadata>()

            // Get ID Property
            val idProperty = binding.identifierProperty
            val idMeta = getFieldMetadata(idProperty)
            fields.put(idMeta.name, idMeta)

            // Get Other Columns
            val propertyIterator = binding.propertyIterator
            while (propertyIterator.hasNext()) {
                val property = propertyIterator.next() as Property
                val meta = getFieldMetadata(property)
                fields.put(meta.name, meta)
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
