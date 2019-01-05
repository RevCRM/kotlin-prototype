package org.revcrm.meta

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.revcrm.annotations.Label
import javax.validation.constraints.NotBlank
import kotlin.reflect.full.findAnnotation

class EntityWithPropertyAnnotation(
    @Label("Label Annotation")
    val property: String
)

class EntityWithGetterAnnotation(
    @get:NotBlank
    var property: String
)

class EntityWithFieldAnnotation(
    @field:NotBlank
    var property: String
)

class EntityPropInfoTests {

    @Test
    fun `returns expected basic property information`() {
        val propInfo = EntityPropInfo(EntityWithPropertyAnnotation::class, "property")
        assertThat(propInfo.name).isEqualTo("property")
        assertThat(propInfo.jvmType).isEqualTo("java.lang.String")
        assertThat(propInfo.nullable).isFalse()
    }

    @Test
    fun `I can read a kotlin property annotation`() {
        val propInfo = EntityPropInfo(EntityWithPropertyAnnotation::class, "property")
        val annotation = propInfo.property.findAnnotation<Label>()!!
        assertThat(annotation.label).isEqualTo("Label Annotation")
    }

    @Test
    fun `I can read a getter annotation using findJavaAnnotation()`() {
        val propInfo = EntityPropInfo(EntityWithGetterAnnotation::class, "property")
        val annotation = propInfo.findJavaAnnotation(NotBlank::class.java)
        assertThat(annotation).isNotNull()
    }

    @Test
    fun `I can read a field annotation using findJavaAnnotation()`() {
        val propInfo = EntityPropInfo(EntityWithFieldAnnotation::class, "property")
        val annotation = propInfo.findJavaAnnotation(NotBlank::class.java)
        assertThat(annotation).isNotNull()
    }
}