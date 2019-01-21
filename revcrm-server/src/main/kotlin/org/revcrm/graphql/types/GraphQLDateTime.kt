package org.revcrm.graphql.types

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.LocalDateTime

// We cant use the ExtendedScalars type because Morphia does not support Offset date/times
val GraphQLDateTime = GraphQLScalarType.newScalar()
    .name("DateTime")
    .coercing(object : Coercing<LocalDateTime, String> {
        override fun serialize(input: Any): String {
            if (input is LocalDateTime)
                return input.toString()
            else
                throw CoercingSerializeException(
                    "Expected LocalDateTime but was $input."
                )
        }

        override fun parseValue(input: Any): LocalDateTime {
            if (input is LocalDateTime)
                return input
            else if (input is String)
                return parseString(input, ::CoercingParseValueException)
            else
                throw CoercingParseValueException(
                    "Expected a String but was $input."
                )
        }

        override fun parseLiteral(input: Any): LocalDateTime {
            if (input !is StringValue) {
                throw CoercingParseLiteralException(
                    "Expected AST type 'StringValue' but was $input."
                )
            }
            return parseString(input.value, ::CoercingParseLiteralException)
        }

        private fun parseString(s: String, errorCtor: (m: String) -> RuntimeException): LocalDateTime {
            try {
                return LocalDateTime.parse(s)
            } catch (e: Error) {
                throw errorCtor("Invalid DateTime value '$s': ${e.message}")
            }
        }
    })
    .build()