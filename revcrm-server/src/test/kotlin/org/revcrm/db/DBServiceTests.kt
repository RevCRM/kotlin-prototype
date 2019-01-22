package org.revcrm.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TEST_DB_CONFIG
import org.revcrm.testdb.TestDB

class DBServiceTests {

    val testDB = TestDB.instance

    @Nested
    inner class EntityMappings {

        @Test
        fun `getEntityClassNames() returns entityClasses list from config`() {
            val configuredClasses = TEST_DB_CONFIG.entityClasses
            val returnedClasses = testDB.getEntityClassNames()
            assertThat(returnedClasses).isEqualTo(configuredClasses)
        }

        @Test
        fun `getEmbeddedClassNames() returns embeddedClasses list from config`() {
            val configuredClasses = TEST_DB_CONFIG.embeddedClasses
            val returnedClasses = testDB.getEmbeddedClassNames()
            assertThat(returnedClasses).isEqualTo(configuredClasses)
        }

        @Test
        fun `classIsEntity() returns true for entity class`() {
            val entityClass = TEST_DB_CONFIG.entityClasses[0]
            assertThat(testDB.classIsEntity(entityClass)).isTrue()
        }

        @Test
        fun `classIsEntity() returns false for non-entity class`() {
            val nonEntityClass = TEST_DB_CONFIG.embeddedClasses[0]
            assertThat(testDB.classIsEntity(nonEntityClass)).isFalse()
        }

        @Test
        fun `classIsEmbeddedEntity() returns true for embedded class`() {
            val embeddedClass = TEST_DB_CONFIG.embeddedClasses[0]
            assertThat(testDB.classIsEmbeddedEntity(embeddedClass)).isTrue()
        }

        @Test
        fun `classIsEmbeddedEntity() returns false for non-embedded class`() {
            val nonEmbeddedClass = TEST_DB_CONFIG.entityClasses[0]
            assertThat(testDB.classIsEmbeddedEntity(nonEmbeddedClass)).isFalse()
        }
    }
}
