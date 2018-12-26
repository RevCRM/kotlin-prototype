package org.revcrm.graphql

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import graphql.ExecutionResult
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.data.FieldService
import org.revcrm.testdb.Account
import org.revcrm.testdb.TEST_ACCOUNTS
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.objectIdDeserializer
import org.revcrm.testdb.resetAccountData

class ReadingData {

    val testDB = TestDB.instance
    val api = APIService(testDB, FieldService())
    val gson = GsonBuilder()
        .registerTypeAdapter(ObjectId::class.java, objectIdDeserializer)
        .create()

    init {
        testDB.withDB { ds ->
            resetAccountData(ds)
        }
        api.initialise()
    }

    fun <T> getResults(res: ExecutionResult, entityKey: String, resultClass: Class<T>): EntitySearchResults<T> {
        assertThat(res.errors).hasSize(0)
        val entityData = res.getData<Map<String, Any>>()
        val tree = gson.toJsonTree(entityData.get(entityKey))
        return gson.fromJson(tree, TypeToken.getParameterized(
            EntitySearchResults::class.java,
            resultClass
        ).type)
    }

    @Nested
    inner class Query_NoArgs {

        val res = api.query("""
                query {
                    Account {
                        results {
                            id
                            name
                            phone
                            email
                            rating
                        }
                    }
                }
            """.trimIndent(), mapOf())
        val result = getResults(res, "Account", Account::class.java)

        @Test
        fun `returns all rows`() {
            assertThat(result.results).hasSize(TEST_ACCOUNTS.size)
            assertThat(result.results[0].name).isEqualTo(TEST_ACCOUNTS[0].name)
            assertThat(result.results[1].name).isEqualTo(TEST_ACCOUNTS[1].name)
            assertThat(result.results[2].name).isEqualTo(TEST_ACCOUNTS[2].name)
        }
    }
}