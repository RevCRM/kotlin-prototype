package org.revcrm.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB

class DBServiceTests {

    val testDB = TestDB.instance
    val entities = testDB.getEntityMappings()

    @Nested
    inner class EntityMappings {

        @Test
        fun `returns only entities in the entityClasses list`() {
            val entityClassNames = mutableListOf<String>()
            entityClassNames.addAll(testDB.getEntityClassNames())
            entityClassNames.addAll(testDB.getEmbeddedClassNames())

            assertThat(entities).allMatch { entity ->
                entityClassNames.find { it == entity.clazz.name } != null
            }
        }
    }
}
