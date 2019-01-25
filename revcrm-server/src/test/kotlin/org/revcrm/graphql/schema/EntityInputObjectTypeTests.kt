package org.revcrm.graphql.schema

import graphql.Scalars
import graphql.schema.GraphQLInputObjectType
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.revcrm.annotations.APIDisabled
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.graphql.types.GraphQLObjectID
import org.revcrm.meta.MetadataService
import xyz.morphia.annotations.Id

class TestInputEntity(
    @Id
    var id_field: ObjectId,
    var int_field: Int,
    var string_field: String,
    @APIDisabled
    var api_disabled_field: String
) {
    val readonly_field: String
    get() = string_field
}

val entityClasses = listOf(
    "org.revcrm.graphql.schema.TestInputEntity"
)

class EntityInputObjectTypeTests {

    /**
     * Create mock DBService
     */
    val data = DBService().apply {
        mockkObject(this)
    }
    init {
        every { data.getEntityClassNames() } returns entityClasses
        every { data.getEmbeddedClassNames() } returns listOf()
    }

    /**
     * Instantiate MetadataService and APIService
     */
    val meta = MetadataService(data).apply { initialise() }
    val api = APIService(data, meta).apply { initialise() }

    val inputType = api.graphQLSchema.getType("TestInputEntityInput") as GraphQLInputObjectType

    @Test
    fun `All input fields are nullable (for updates)`() {
        val idType = inputType.getFieldDefinition("id_field").type
        val intType = inputType.getFieldDefinition("int_field").type
        val stringType = inputType.getFieldDefinition("string_field").type
        Assertions.assertThat(idType).isEqualTo(GraphQLObjectID) // i.e. nullable
        Assertions.assertThat(intType).isEqualTo(Scalars.GraphQLInt) // i.e. nullable
        Assertions.assertThat(stringType).isEqualTo(Scalars.GraphQLString) // i.e. nullable
    }

    @Test
    fun `Fields with apiEnabled = false are not exposed`() {
        Assertions.assertThat(inputType.getFieldDefinition("api_disabled_field")).isNull()
    }

    @Test
    fun `Readonly ("val") fields are not exposed`() {
        Assertions.assertThat(inputType.getFieldDefinition("readonly_field")).isNull()
    }
}