package org.revcrm.graphql.updating

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.graphql.mapGraphQLMutationResult
import org.revcrm.meta.MetadataService
import org.revcrm.testdb.Account
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.deleteAccountData

fun setUpData(db: DBService): ObjectId {
    val account = Account(
        name = "Initial Name",
        contact = "Initial Contact",
        phone = "Initial Phone",
        email = "Initial Email",
        rating = 1
    )
    db.withDB { ds ->
        deleteAccountData(ds)
        ds.save(account)
    }
    return account.id!!
}

class UpdatingDataTests {

    val testDB = TestDB.instance
    val meta = MetadataService(testDB)
    val api = APIService(testDB, meta)

    @Nested
    inner class UpdateFullRecord {
        val recordId: ObjectId

        init {
            recordId = setUpData(testDB)
        }

        val data = "\$data"
        val res = api.query(
            """
                mutation ($data: AccountInput!) {
                    updateAccount (data: $data) {
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
                    "id" to recordId,
                    "name" to "New Name",
                    "contact" to "New Contact",
                    "phone" to "New Phone",
                    "email" to "New Email",
                    "rating" to 100
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateAccount", Account::class.java)

        @Test
        fun `We can update a full object`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.id).isEqualTo(recordId)
            assertThat(result.result.name).isEqualTo("New Name")
            assertThat(result.result.contact).isEqualTo("New Contact")
            assertThat(result.result.phone).isEqualTo("New Phone")
            assertThat(result.result.email).isEqualTo("New Email")
            assertThat(result.result.rating).isEqualTo(100)
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `updated record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(Account::class.java, recordId)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("New Name")
            }
        }
    }

    @Nested
    inner class UpdatePartialRecord {
        val recordId: ObjectId

        init {
            recordId = setUpData(testDB)
        }

        val data = "\$data"
        val res = api.query(
            """
                mutation ($data: AccountInput!) {
                    updateAccount (data: $data) {
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
                    "id" to recordId,
                    "phone" to "New Phone",
                    "email" to "New Email",
                    "rating" to 100
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateAccount", Account::class.java)

        @Test
        fun `We can update a subset of fields`() {
            assertThat(result.result).isNotNull()
            assertThat(result.result.id).isEqualTo(recordId)
            assertThat(result.result.name).isEqualTo("Initial Name")
            assertThat(result.result.contact).isEqualTo("Initial Contact")
            assertThat(result.result.phone).isEqualTo("New Phone")
            assertThat(result.result.email).isEqualTo("New Email")
            assertThat(result.result.rating).isEqualTo(100)
        }

        @Test
        fun `returns expected validation data`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.entityErrors).hasSize(0)
            assertThat(result.validation.fieldErrors).hasSize(0)
        }

        @Test
        fun `updated record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.get(Account::class.java, recordId)
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Initial Name")
                assertThat(match.phone).isEqualTo("New Phone")
            }
        }
    }

    @Nested
    inner class UpdateValidationErrors {
        val recordId: ObjectId

        init {
            recordId = setUpData(testDB)
        }

        val data = "\$data"
        val res = api.query(
            """
                mutation ($data: AccountInput!) {
                    updateAccount (data: $data) {
                        result {
                            id
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
                    "id" to recordId,
                    "name" to "",
                    "contact" to "invalid record"
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateAccount", Account::class.java)

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

    @Nested
    inner class NoIDSpecified {

        init {
            testDB.withDB { ds -> deleteAccountData(ds) }
        }

        val data = "\$data"
        val res = api.query(
            """
                mutation ($data: AccountInput!) {
                    updateAccount (data: $data) {
                        result {
                            id
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                entity
                                entityPath
                                code
                                message
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
                    "name" to "New Name",
                    "contact" to "New Contact",
                    "phone" to "New Phone",
                    "email" to "New Email",
                    "rating" to 100
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateAccount", Account::class.java)

        @Test
        fun `does not return result`() {
            assertThat(result.result).isNull()
        }

        @Test
        fun `returns expected error`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.fieldErrors).hasSize(0)
            assertThat(result.validation.entityErrors).hasSize(1)
            assertThat(result.validation.entityErrors[0]).matches { err ->
                err.entity == "Account" &&
                    err.entityPath == "" &&
                    err.code == "IDMissing" &&
                    err.message.contains("cannot update without 'id' field")
            }
        }

        @Test
        fun `new record is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(Account::class.java)
                    .field("name").equal("New Name")
                    .get()
                assertThat(match).isNull()
            }
        }
    }

    @Nested
    inner class RecordNotFound {

        init {
            testDB.withDB { ds -> deleteAccountData(ds) }
        }

        val data = "\$data"
        val res = api.query(
            """
                mutation ($data: AccountInput!) {
                    updateAccount (data: $data) {
                        result {
                            id
                        }
                        validation {
                            hasErrors
                            entityErrors {
                                entity
                                entityPath
                                code
                                message
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
                    "id" to "aaaaaaaaaaaaaaaaaaaaaaaa",
                    "phone" to "New Phone",
                    "email" to "New Email"
                )
            )
        )
        val result = mapGraphQLMutationResult(res, "updateAccount", Account::class.java)

        @Test
        fun `does not return result`() {
            assertThat(result.result).isNull()
        }

        @Test
        fun `returns expected error`() {
            assertThat(result.validation).isNotNull()
            assertThat(result.validation.fieldErrors).hasSize(0)
            assertThat(result.validation.entityErrors).hasSize(1)
            assertThat(result.validation.entityErrors[0]).matches { err ->
                err.entity == "Account" &&
                    err.entityPath == "" &&
                    err.code == "IDNotFound" &&
                    err.message.contains("the specified 'id' was not found")
            }
        }

        @Test
        fun `new record is NOT stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(Account::class.java)
                    .field("phone").equal("New Phone")
                    .get()
                assertThat(match).isNull()
            }
        }
    }
}