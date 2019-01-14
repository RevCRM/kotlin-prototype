package org.revcrm.db

import javax.validation.ConstraintViolation

class EntityError(
    val entity: String,
    val entityPath: String,
    val code: String,
    val message: String
)

class FieldError(
    val entity: String,
    val fieldPath: String,
    val code: String,
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
                    code = errorCode,
                    message = vio.message
                ))
            } else {
                fieldErrors.add(FieldError(
                    entity = vio.leafBean::class.java.simpleName,
                    fieldPath = vio.propertyPath.toString(),
                    code = errorCode,
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
            code = errorCode,
            message = message
        ))
    }

    fun addFieldError(obj: Any, fieldPath: String, errorCode: String, message: String) {
        fieldErrors.add(FieldError(
            entity = obj.javaClass.simpleName,
            fieldPath = fieldPath,
            code = errorCode,
            message = message
        ))
    }
}
