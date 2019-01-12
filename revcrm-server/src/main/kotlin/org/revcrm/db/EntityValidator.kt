package org.revcrm.db

import com.mongodb.DBObject
import xyz.morphia.AbstractEntityInterceptor
import xyz.morphia.mapping.Mapper
import javax.validation.Validation
import javax.validation.Validator

interface Validate {
    fun validate(errors: EntityValidationData)
}

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
        if (ent is Validate) {
            ent.validate(errorData)
        }
        if (errorData.hasErrors())
            throw EntityValidationError(errorData)
    }
}