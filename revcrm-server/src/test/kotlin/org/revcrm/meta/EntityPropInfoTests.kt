package org.revcrm.meta

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.annotations.APIDisabled
import org.revcrm.annotations.Label
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Reference
import javax.validation.constraints.NotBlank
import kotlin.reflect.full.findAnnotation

class EntityWithPropertyAnnotation(
    @Label("Label Annotation")
    var property: String
)

class EntityWithGetterAnnotation(
    @get:NotBlank
    var property: String
)

class EntityWithFieldAnnotation(
    @field:NotBlank
    var property: String
)

class EntityWithFieldAPIDisabled(
    var normalProperty: String,

    @APIDisabled
    var disabledProperty: String
)

class EntityWithImmutableField {
    val immutableProperty: String
        get() = this::class.simpleName!!
}

class EntityWithIdField(
    @Id
    var some_id_field: String,
    var other_field: String
)

class EntityWithEmbeddedEntityField(
    var id: String,
    @Embedded
    var entity: EmbeddedEntity
)

class EntityWithEntityReferenceField(
    var id: String,
    @Reference
    var entity: EntityWithIdField
)

class EntityPropInfoTests {

    @Test
    fun `returns expected basic property information`() {
        val propInfo = getEntityPropInfo(EntityWithPropertyAnnotation::class, "property")
        assertThat(propInfo.name).isEqualTo("property")
        assertThat(propInfo.jvmType).isEqualTo("java.lang.String")
        assertThat(propInfo.isNullable).isFalse()
        assertThat(propInfo.isImmutable).isFalse()
    }

    @Test
    fun `I can read a kotlin property annotation`() {
        val propInfo = getEntityPropInfo(EntityWithPropertyAnnotation::class, "property")
        val annotation = propInfo.property.findAnnotation<Label>()!!
        assertThat(annotation.label).isEqualTo("Label Annotation")
    }

    @Test
    fun `I can read a getter annotation using findJavaAnnotation()`() {
        val propInfo = getEntityPropInfo(EntityWithGetterAnnotation::class, "property")
        val annotation = propInfo.findJavaAnnotation(NotBlank::class.java)
        assertThat(annotation).isNotNull()
    }

    @Test
    fun `I can read a field annotation using findJavaAnnotation()`() {
        val propInfo = getEntityPropInfo(EntityWithFieldAnnotation::class, "property")
        val annotation = propInfo.findJavaAnnotation(NotBlank::class.java)
        assertThat(annotation).isNotNull()
    }

    @Nested
    inner class APIDisabledFields {

        @Test
        fun `Properties have apiEnabled = true by default`() {
            val propInfo = getEntityPropInfo(EntityWithFieldAPIDisabled::class, "normalProperty")
            assertThat(propInfo.isApiEnabled).isTrue()
        }

        @Test
        fun `Properties have apiEnabled = false when @APIDisabled is applied`() {
            val propInfo = getEntityPropInfo(EntityWithFieldAPIDisabled::class, "disabledProperty")
            assertThat(propInfo.isApiEnabled).isFalse()
        }
    }

    @Nested
    inner class ImmutableFields {

        @Test
        fun `immutable properties have isImmutable = true`() {
            val propInfo = getEntityPropInfo(EntityWithImmutableField::class, "immutableProperty")
            assertThat(propInfo.isImmutable).isTrue()
        }
    }

    @Nested
    inner class IDFields {

        @Test
        fun `Properties with @Id annotation have isIdField = true`() {
            val propInfo = getEntityPropInfo(EntityWithIdField::class, "some_id_field")
            assertThat(propInfo.isIdField).isTrue()
        }

        @Test
        fun `Properties without @Id annotation have isIdField = false`() {
            val propInfo = getEntityPropInfo(EntityWithIdField::class, "other_field")
            assertThat(propInfo.isIdField).isFalse()
        }
    }

    @Nested
    inner class EmbeddedEntityFields {

        @Test
        fun `Properties with @Embedded annotation have isEmbedded = true`() {
            val propInfo = getEntityPropInfo(EntityWithEmbeddedEntityField::class, "entity")
            assertThat(propInfo.isEmbedded).isTrue()
        }

        @Test
        fun `Properties without @Embedded annotation have isEmbedded = false`() {
            val propInfo = getEntityPropInfo(EntityWithEmbeddedEntityField::class, "id")
            assertThat(propInfo.isEmbedded).isFalse()
        }
    }

    @Nested
    inner class EntityReferenceFields {

        @Test
        fun `Properties with @Reference annotation have isReference = true`() {
            val propInfo = getEntityPropInfo(EntityWithEntityReferenceField::class, "entity")
            assertThat(propInfo.isReference).isTrue()
        }

        @Test
        fun `Properties without @Reference annotation have isReference = false`() {
            val propInfo = getEntityPropInfo(EntityWithEntityReferenceField::class, "id")
            assertThat(propInfo.isReference).isFalse()
        }
    }
}