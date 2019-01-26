package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLOutputType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import javax.validation.constraints.Max
import javax.validation.constraints.Min

open class IntegerField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return Scalars.GraphQLInt
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return Scalars.GraphQLInt
    }
}

open class FloatField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return Scalars.GraphQLFloat
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return Scalars.GraphQLFloat
    }
}

open class DecimalField(
    propInfo: EntityPropInfo,
    properties: Map<String, String> = mapOf(),
    constraints: Map<String, String> = mapOf()
) : Field(propInfo, properties, constraints) {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLOutputType {
        return Scalars.GraphQLBigDecimal
    }
    override fun getGraphQLInputType(meta: MetadataService, entity: Entity): GraphQLInputType {
        return Scalars.GraphQLBigDecimal
    }
}

private fun getNumericConstraints(propInfo: EntityPropInfo): Map<String, String> {
    val constraints = mutableMapOf<String, String>()
    val minAnnotation = propInfo.findJavaAnnotation(Min::class.java)
    val maxAnnotation = propInfo.findJavaAnnotation(Max::class.java)
    if (minAnnotation != null) {
        constraints.set("Min", minAnnotation.value.toString())
    }
    if (maxAnnotation != null) {
        constraints.set("Max", maxAnnotation.value.toString())
    }
    return constraints
}

@Suppress("UNUSED_PARAMETER")
fun mapIntegerField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return IntegerField(
        propInfo,
        constraints = getNumericConstraints(propInfo)
    )
}

@Suppress("UNUSED_PARAMETER")
fun mapFloatField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return FloatField(
        propInfo,
        constraints = getNumericConstraints(propInfo)
    )
}

@Suppress("UNUSED_PARAMETER")
fun mapDecimalField(meta: MetadataService, propInfo: EntityPropInfo): Field {
    return DecimalField(
        propInfo,
        constraints = getNumericConstraints(propInfo)
    )
}