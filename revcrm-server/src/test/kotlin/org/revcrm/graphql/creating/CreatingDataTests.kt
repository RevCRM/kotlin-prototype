package org.revcrm.graphql.creating

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.Account
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.deleteAccountData

class CreatingDataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    init {
        testDB.withDB { ds ->
            deleteAccountData(ds)
        }
        meta.initialise()
        api.initialise()
    }

    @Nested
    inner class CreateRecord {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: AccountInput!) {
                    createAccount (data: $data) {
                        result {
                            id
                            name
                            contact
                            phone
                            email
                            rating
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                code
                            }
                            fieldErrors {
                                code
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "data" to mapOf(
                    "name" to "Test Name",
                    "contact" to "Test Contact",
                    "phone" to "Test Phone",
                    "email" to "Test Email",
                    "rating" to 10
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createAccount", Account::class.java)

        @Test
        fun `returns new Account data and ID`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.name).isEqualTo("Test Name")
            assertThat(result.result.contact).isEqualTo("Test Contact")
            assertThat(result.result.phone).isEqualTo("Test Phone")
            assertThat(result.result.email).isEqualTo("Test Email")
            assertThat(result.result.rating).isEqualTo(10)
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `new record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(Account::class.java)
                    .field("_id").equal(result.result.id)
                    .get()
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Test Name")
            }
        }
    }

    @Nested
    inner class CreateValidationErrors {

        val data = "\$data"
        val res = api.query("""
                mutation ($data: AccountInput!) {
                    createAccount (data: $data) {
                        result {
                            id
                            name
                            contact
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                code
                            }
                            fieldErrors {
                                entity
                                fieldPath
                                code
                                message
                            }
                        }
                    }
                }
            """.trimIndent(),
            mapOf(
                "data" to mapOf(
                    "name" to "",
                    "contact" to "invalid record"
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "createAccount", Account::class.java)

        @Test
        fun `does not return result`() {
            assertThat(result.result).isNull()
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors.size).isGreaterThan(0)
            assertThat(result.validation.fieldErrors).anyMatch { err ->
                err.entity == "Account" &&
                err.fieldPath == "name" &&
                err.code == "NotBlank" &&
                err.message.contains("must not be blank")
            }
        }

        @Test
        fun `new record is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(Account::class.java)
                    .field("contact").equal("invalid record")
                    .get()
                assertThat(match).isNull()
            }
        }
    }
}