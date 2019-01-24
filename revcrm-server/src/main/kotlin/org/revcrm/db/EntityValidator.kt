package org.revcrm.db

import com.mongodb.DBObject
import com.mongodb.WriteResult
import org.revcrm.annotations.Validate
import org.revcrm.annotations.ValidateDelete
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.Datastore
import xyz.morphia.mapping.Mapper
import javax.validation.Validation
import javax.validation.Validator
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

class EntityValidator(
    private val db: DBService
) : AbstractEntityInterceptor() {
    val validator: Validator

    init {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    override fun prePersist(ent: Any, dbObj: DBObject, mapper: Mapper) {
        val violations = validator.validate(ent)
        val errorData: EntityValidationData
        if (!violations.isEmpty())
            errorData = EntityValidationData(violations)
        else
            errorData = EntityValidationData()

        // Scan members for null values in non-nullable fields (waiting on an answer to:
        //      https://stackoverflow.com/questions/45798898/kotlin-and-javax-validation-constraints-notnull)
        val clazz = ent.javaClass.kotlin
        clazz.memberProperties.forEach { prop ->
            if (!prop.returnType.isMarkedNullable) {
                val value = prop.get(ent)
                if (value == null) {
                    errorData.addFieldError(
                        obj = ent,
                        fieldPath = prop.name,
                        errorCode = "NotNull",
                        message = "must not be null"
                    )
                }
            }
        }

        // Call any @Validate entity function
        // TODO: Searches class members on every call, so could be optimised...
        val member = ent::class.functions.find { m -> m.findAnnotation<Validate>() != null }
        if (member != null) {
            val method = member as KFunction
            method.call(ent, errorData)
        }

        if (errorData.hasErrors())
            throw EntityValidationError(errorData)
    }
}

fun <T : Any> Datastore.deleteWithValidation(entity: T): WriteResult {

    // Call any @ValidateDelete entity function
    // TODO: Searches class members on every call so needs optimising!
    val member = entity::class.members.find { m -> m.findAnnotation<ValidateDelete>() != null }
    if (member != null) {
        val method = member as KFunction
        val errorData = EntityValidationData()
        method.call(entity, errorData)

        if (errorData.hasErrors())
            throw EntityValidationError(errorData)
    }
    return this.delete(entity)
}