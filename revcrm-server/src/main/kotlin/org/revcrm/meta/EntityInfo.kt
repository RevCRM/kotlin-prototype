package org.revcrm.meta

import org.revcrm.annotations.APIDisabled
import kotlin.reflect.full.findAnnotation

/**
 * Takes a KClass and extracts various
 * metadata needed by MetadataService
 */
class EntityInfo(
    val className: String
) {
    val klass = Class.forName(className).kotlin
    val entityName = klass.simpleName!!
    val isApiEnabled = klass.findAnnotation<APIDisabled>() == null
}