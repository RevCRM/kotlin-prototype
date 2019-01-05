package org.revcrm.meta

import org.revcrm.annotations.Label
import org.revcrm.util.getProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * Takes a KClass and a property name and extracts various
 * metadata needed by MetadataService and IFields
 */
class EntityPropInfo(
    val klass: KClass<*>,
    val name: String
) {
    val property: KProperty1<*, *>
    val label: String
    val jvmType: String
    val nullable: Boolean
    val isEnum: Boolean

    init {
        val foundProperty = getProperty(klass, name)
        if (foundProperty == null)
            throw Error("Could not retrieve property '${klass.simpleName}.$name'.")

        property = foundProperty
        nullable = property.returnType.isMarkedNullable
        var field = property.javaField!!
        jvmType = field.type.name
        isEnum = field.type.isEnum

        val labelAnnotation = property.findAnnotation<Label>()
        label = if (labelAnnotation != null) labelAnnotation.label
            else name
    }

    fun <T : Annotation> findJavaAnnotation(annotationClass: Class<T>): T? {
        var annotation = property.javaGetter?.getAnnotation(annotationClass)
        if (annotation == null)
            annotation = property.javaField?.getAnnotation(annotationClass)
        return annotation
    }
}