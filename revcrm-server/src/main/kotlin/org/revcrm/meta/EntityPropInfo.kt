package org.revcrm.meta

import org.revcrm.annotations.APIDisabled
import org.revcrm.annotations.Label
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Reference
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
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
    val property: KProperty1<*, *>
) {
    val name: String
    val label: String
    val jvmType: String
    val isNullable: Boolean
    val isImmutable: Boolean
    val isApiEnabled: Boolean
    val isEnum: Boolean
    val isIdField: Boolean
    val isEmbedded: Boolean
    val isReference: Boolean

    init {
        name = property.name
        isNullable = property.returnType.isMarkedNullable
        isApiEnabled = property.findAnnotation<APIDisabled>() == null
        if (property is KMutableProperty<*>) {
            isImmutable = false
            var field = property.javaField!!
            jvmType = field.type.name
            isEnum = field.type.isEnum
            isIdField = field.getAnnotation(Id::class.java) != null
            isEmbedded = field.getAnnotation(Embedded::class.java) != null
            isReference = field.getAnnotation(Reference::class.java) != null
        } else {
            isImmutable = true
            isIdField = false
            isEmbedded = false
            isReference = false
            var getter = property.javaGetter!!
            jvmType = getter.returnType.name
            isEnum = getter.returnType.isEnum
        }

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