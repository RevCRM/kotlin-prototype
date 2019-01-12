package org.revcrm.db

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

fun isEntityError(vio: ConstraintViolation<*>): Boolean {
    // Ideally we would check elementType but it is a private member,
    // so instead we check that the "value" is the same as the leaf bean
    return vio.leafBean == vio.invalidValue
}

fun getValidationMessage(violations: Set<ConstraintViolation<*>>): String {
    val sb = StringBuilder()
    sb.append("Entity failed validation:\n")
    violations.forEach { vio ->
        sb.append(vio.rootBeanClass.simpleName)
        if (vio.propertyPath.toString().isNotBlank()) {
            sb.append(".")
            sb.append(vio.propertyPath)
        }
        sb.append(": ")
        sb.append(vio.message)
        if (isEntityError(vio)) {
            sb.append(". Entity: ")
            sb.append(vio.invalidValue)
            sb.append("'\n")
        } else {
            sb.append(". Value: '")
            sb.append(vio.invalidValue)
            sb.append("'\n")
        }
    }
    return sb.toString()
}

class EntityError(
    val entity: String,
    val entityPath: String,
    val errorCode: String,
    val message: String
)

class FieldError(
    val entity: String,
    val fieldPath: String,
    val errorCode: String,
    val message: String
)

class EntityValidationErrorData(
    val entityErrors: List<EntityError>,
    val fieldErrors: List<FieldError>
)

class EntityValidationError(
    vio: Set<ConstraintViolation<*>>
) : ConstraintViolationException(
    getValidationMessage(vio), vio
) {
    fun getValidationErrorData(): EntityValidationErrorData {
        val fieldErrors = mutableListOf<FieldError>()
        val entityErrors = mutableListOf<EntityError>()
        constraintViolations.forEach { vio ->

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
        return EntityValidationErrorData(entityErrors, fieldErrors)
    }
}