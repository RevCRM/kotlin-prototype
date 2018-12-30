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
    if (propInfo.field.isAnnotationPresent(Min::class.java)) {
        val min = propInfo.field.getAnnotation(Min::class.java)
        constraints.set("Min", min.value.toString())
    }
    if (propInfo.field.isAnnotationPresent(Max::class.java)) {
        val max = propInfo.field.getAnnotation(Max::class.java)
        constraints.set("Max", max.value.toString())
    }
    return constraints
}

fun mapIntegerField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return IntegerField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}

fun mapFloatField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return FloatField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}

fun mapDecimalField(meta: MetadataService, propInfo: EntityPropInfo): IField {
    return DecimalField(
        name = propInfo.name,
        label = propInfo.name, // TODO: Get from @Field annotation
        jvmType = propInfo.jvmType,
        nullable = propInfo.nullable,
        constraints = getNumericConstraints(propInfo)
    )
}