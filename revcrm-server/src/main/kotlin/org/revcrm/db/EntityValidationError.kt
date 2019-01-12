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

class EntityValidationError(
    val errorData: EntityValidationData
) : ConstraintViolationException(
    getValidationMessage(errorData.violations), errorData.violations
)