package org.revcrm.db

import com.mongodb.DBObject
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.mapping.Mapper
import javax.validation.Validation
import javax.validation.Validator

class EntityValidator : AbstractEntityInterceptor() {
    val validator: Validator

    init {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    override fun prePersist(ent: Any, dbObj: DBObject, mapper: Mapper) {
        val violations = validator.validate(ent)
        if (!violations.isEmpty())
            throw EntityValidationException(violations)
    }
}