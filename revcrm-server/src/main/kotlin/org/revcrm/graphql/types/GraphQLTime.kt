package org.revcrm.graphql.types

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.LocalTime

// We cant use the ExtendedScalars type because Morphia does not support Offset date/times
val GraphQLTime = GraphQLScalarType.newScalar()
    .name("Time")
    .coercing(object : Coercing<LocalTime, String> {
        override fun serialize(input: Any): String {
            if (input is LocalTime)
                return input.toString()
            else
                throw CoercingSerializeException(
                    "Expected LocalTime but was $input."
                )
        }

        override fun parseValue(input: Any): LocalTime {
            if (input is LocalTime)
                return input
            else if (input is String)
                return parseString(input, ::CoercingParseValueException)
            else
                throw CoercingParseValueException(
                    "Expected a String but was $input."
                )
        }

        override fun parseLiteral(input: Any): LocalTime {
            if (input !is StringValue) {
                throw CoercingParseLiteralException(
                    "Expected AST type 'StringValue' but was $input."
                )
            }
            return parseString(input.value, ::CoercingParseLiteralException)
        }

        private fun parseString(s: String, errorCtor: (m: String) -> RuntimeException): LocalTime {
            try {
                return LocalTime.parse(s)
            } catch (e: Error) {
                throw errorCtor("Invalid Time value '$s': ${e.message}")
            }
        }
    })
    .build()