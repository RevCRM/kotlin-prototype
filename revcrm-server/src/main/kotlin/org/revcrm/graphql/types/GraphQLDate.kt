package org.revcrm.graphql.types

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.LocalDate

// We cant use the ExtendedScalars type because Morphia does not support Offset date/times
val GraphQLDate = GraphQLScalarType.newScalar()
    .name("Date")
    .coercing(object : Coercing<LocalDate, String> {
        override fun serialize(input: Any): String {
            if (input is LocalDate)
                return input.toString()
            else
                throw CoercingSerializeException(
                    "Expected LocalDate but was $input."
                )
        }

        override fun parseValue(input: Any): LocalDate {
            if (input is LocalDate)
                return input
            else if (input is String)
                return parseString(input, ::CoercingParseValueException)
            else
                throw CoercingParseValueException(
                    "Expected a String but was $input."
                )
        }

        override fun parseLiteral(input: Any): LocalDate {
            if (input !is StringValue) {
                throw CoercingParseLiteralException(
                    "Expected AST type 'StringValue' but was $input."
                )
            }
            return parseString(input.value, ::CoercingParseLiteralException)
        }

        private fun parseString(s: String, errorCtor: (m: String) -> RuntimeException): LocalDate {
            try {
                return LocalDate.parse(s)
            } catch (e: Error) {
                throw errorCtor("Invalid Date value '$s': ${e.message}")
            }
        }
    })
    .build()