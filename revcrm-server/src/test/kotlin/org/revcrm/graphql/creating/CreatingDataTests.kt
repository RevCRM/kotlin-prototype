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
                        id
                        name
                        contact
                        phone
                        email
                        rating
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
            assertThat(result.id).isNotNull()
            assertThat(result.name).isEqualTo("Test Name")
            assertThat(result.contact).isEqualTo("Test Contact")
            assertThat(result.phone).isEqualTo("Test Phone")
            assertThat(result.email).isEqualTo("Test Email")
            assertThat(result.rating).isEqualTo(10)
        }

        @Test
        fun `new record is stored in the database`() {
            testDB.withDB { ds ->
                val match = ds.createQuery(Account::class.java)
                    .field("_id").equal(result.id)
                    .get()
                assertThat(match).isNotNull()
                assertThat(match.name).isEqualTo("Test Name")
            }
        }
    }
}