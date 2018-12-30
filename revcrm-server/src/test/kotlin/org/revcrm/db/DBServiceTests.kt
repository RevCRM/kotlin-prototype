package org.revcrm.db

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.revcrm.testdb.TestDB
import xyz.morphia.annotations.Entity
import kotlin.reflect.full.findAnnotation

class DBServiceTests {

    val testDB = TestDB.instance
    val entities = testDB.getEntityMappings()

    @Nested
    inner class EntityMappings {

        @Test
        fun `returns only entities with @Entity annotation`() {
            assertThat(entities).allMatch { entity ->
                entity.clazz.kotlin.findAnnotation<Entity>() != null
            }
        }
    }
}
