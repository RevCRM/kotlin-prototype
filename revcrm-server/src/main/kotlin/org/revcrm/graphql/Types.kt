package org.revcrm.graphql

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import org.bson.types.ObjectId

val GraphQLObjectID = GraphQLScalarType("ID", "GraphQLID Type for BSON ObjectIDs", object : Coercing<Any, Any> {

    private fun convertImpl(input: Any): String? {
        if (input is String) {
            return input
        }
        if (input is ObjectId) {
            return input.toString()
        }
        return null
    }

    override fun serialize(input: Any): String {
        return convertImpl(input) ?: throw CoercingSerializeException(
            "Expected type 'ID' but was '" + input.javaClass.name + "'."
        )
    }

    override fun parseValue(input: Any): String {
        return convertImpl(input) ?: throw CoercingParseValueException(
            "Expected type 'ID' but was '" + input.javaClass.name + "'."
        )
    }

    override fun parseLiteral(input: Any): String {
        if (input is StringValue) {
            return input.value
        }
        throw CoercingParseLiteralException(
            "Expected AST type 'StringValue' but was '" + input.javaClass.name + "'."
        )
    }
})