package org.revcrm.db

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

fun getValidationMessage(violations: Set<ConstraintViolation<*>>): String {
    val sb = StringBuilder()
    sb.append("Entity failed validation:\n")
    violations.forEach { vio ->
        sb.append(vio.rootBeanClass.simpleName)
        sb.append(".")
        sb.append(vio.propertyPath)
        sb.append(": ")
        sb.append(vio.message)
        sb.append(". Value: '")
        sb.append(vio.invalidValue)
        sb.append("'\n")
    }
    return sb.toString()
}

class EntityValidationException(
    vio: Set<ConstraintViolation<*>>
) : ConstraintViolationException(
    getValidationMessage(vio), vio
)