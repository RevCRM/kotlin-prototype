package org.revcrm.db

import com.mongodb.DBObject
import org.revcrm.annotations.OnValidate
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.mapping.Mapper
import javax.validation.Validation
import javax.validation.Validator
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

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

        // Call any @OnValidate entity function
        // TODO: Searches class members on every call so needs optimising!
        val member = ent::class.members.find { m -> m.findAnnotation<OnValidate>() != null }
        if (member != null) {
            val method = member as KFunction
            method.call(ent, errorData)
        }

        if (errorData.hasErrors())
            throw EntityValidationError(errorData)
    }
}