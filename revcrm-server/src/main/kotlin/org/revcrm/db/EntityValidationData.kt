package org.revcrm.db

import javax.validation.ConstraintViolation

class EntityError(
    val entity: String,
    val entityPath: String,
    val errorCode: String,
    val message: String
) {
    constructor(
        obj: Any,
        errorCode: String,
        message: String
    ): this(
        entity = obj.javaClass.simpleName,
        entityPath = "",
        errorCode = errorCode,
        message = message
    )
}

class FieldError(
    val entity: String,
    val fieldPath: String,
    val errorCode: String,
    val message: String
)

class EntityValidationData {
    val entityErrors: MutableList<EntityError>
    val fieldErrors: MutableList<FieldError>
    val violations: Set<ConstraintViolation<*>>

    constructor(vios: Set<ConstraintViolation<*>>) {
        fieldErrors = mutableListOf<FieldError>()
        entityErrors = mutableListOf<EntityError>()
        violations = vios

        violations.forEach { vio ->

            val annotationName = vio.constraintDescriptor.annotation?.annotationClass?.simpleName
            val errorCode = if (annotationName != null) annotationName else ""

            if (isEntityError(vio)) {
                entityErrors.add(EntityError(
                    entity = vio.leafBean::class.java.simpleName,
                    entityPath = vio.propertyPath.toString(),
                    errorCode = errorCode,
                    message = vio.message
                ))
            } else {
                fieldErrors.add(FieldError(
                    entity = vio.leafBean::class.java.simpleName,
                    fieldPath = vio.propertyPath.toString(),
                    errorCode = errorCode,
                    message = vio.message
                ))
            }
        }
    }

    constructor() {
        fieldErrors = mutableListOf()
        entityErrors = mutableListOf()
        violations = setOf()
    }

    fun hasErrors() = entityErrors.size > 0 || fieldErrors.size > 0

    fun addEntityError(obj: Any, errorCode: String, message: String) {
        entityErrors.add(EntityError(
            entity = obj.javaClass.simpleName,
            entityPath = "",
            errorCode = errorCode,
            message = message
        ))
    }
}
