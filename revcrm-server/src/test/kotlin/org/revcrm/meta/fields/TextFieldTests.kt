package org.revcrm.meta.fields

import graphql.Scalars
import io.mockk.mockkClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class EntityWithTextField(
    @Label("Text Field")
    @MultiLine
    @get:NotBlank
    @get:Size(min = 1, max = 10)
    val textField: String
)

class TextFieldTests {

    val propInfo = EntityPropInfo(EntityWithTextField::class, "textField")
    var meta = mockkClass(MetadataService::class)

    val field = mapTextField(meta, propInfo)

    @Test
    fun `maps the standard properties`() {
        assertThat(field.name).isEqualTo("textField")
        assertThat(field.label).isEqualTo("Text Field")
        assertThat(field.type).isEqualTo("TextField")
        assertThat(field.jvmType).isEqualTo("java.lang.String")
        assertThat(field.nullable).isFalse()
    }

    @Test
    fun `maps field properties`() {
        assertThat(field.properties).containsEntry("MultiLine", "true")
    }

    @Test
    fun `maps field constraints`() {
        assertThat(field.constraints).containsEntry("NotBlank", "true")
        assertThat(field.constraints).containsEntry("SizeMin", "1")
        assertThat(field.constraints).containsEntry("SizeMax", "10")
    }

    @Test
    fun `returns the expected GraphQL Type`() {
        val mockEntity = Entity("mock", false, "mock", mapOf())
        val gqlType = field.getGraphQLType(meta, mockEntity)
        assertThat(gqlType).isEqualTo(Scalars.GraphQLString)
    }
}