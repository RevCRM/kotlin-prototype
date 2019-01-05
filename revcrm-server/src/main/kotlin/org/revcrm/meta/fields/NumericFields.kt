package org.revcrm.meta.fields

import graphql.Scalars
import graphql.schema.GraphQLType
import org.revcrm.meta.Entity
import org.revcrm.meta.EntityPropInfo
import org.revcrm.meta.MetadataService
import javax.validation.constraints.Max
import javax.validation.constraints.Min

open class IntegerField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLInt
    }
}

open class FloatField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
        return Scalars.GraphQLFloat
    }
}

open class DecimalField(
    override val name: String,
    override val label: String,
    override val jvmType: String,
    override val nullable: Boolean,
    override val constraints: Map<String, String>
) : IField {

    override fun getGraphQLType(meta: MetadataService, entity: Entity): GraphQLType {
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

fun mapIntegerField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    val test = propInfo.property.annotations
    return IntegerField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}

fun mapFloatField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return FloatField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}

fun mapDecimalField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DecimalField(
        name = propInfo.name,
        label = propInfo.label,
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}