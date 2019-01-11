package org.revcrm.db

import com.mongodb.BasicDBObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.revcrm.testdb.TestConstraintsEntity
import org.revcrm.testdb.TestDB

class ValidationTests {

    val testDB = TestDB.instance

    init {
        testDB.withDB { ds ->
            val col = ds.getCollection(TestConstraintsEntity::class.java)
            col.remove(BasicDBObject())
        }
    }

    @Test
    fun `EntityValidationError is raised when trying to save an invalid entity`() {

        val invalidEntity = TestConstraintsEntity(
            non_nullable_field = "test",
            nullable_field = null,
            textField = "a string that is too long",
            min_field = -1,
            max_field = 101
        )

        val exception = assertThrows<EntityValidationException> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        assertThat(exception.message).contains("Entity failed validation")
        assertThat(exception.constraintViolations).anyMatch { vio ->
            vio.propertyPath.toString() == "textField" &&
                vio.message.contains("size must be between ")
        }
        assertThat(exception.constraintViolations).anyMatch { vio ->
            vio.propertyPath.toString() == "min_field" &&
                vio.message.contains("must be greater than or equal to ")
        }
        assertThat(exception.constraintViolations).anyMatch { vio ->
            vio.propertyPath.toString() == "max_field" &&
                vio.message.contains("must be less than or equal to ")
        }
    }

    @Test
    fun `EntityValidationError is not raised when trying to save a valid entity`() {

        val validEntity = TestConstraintsEntity(
            non_nullable_field = "test",
            nullable_field = null,
            textField = "short str",
            min_field = 10,
            max_field = 20
        )

        assertDoesNotThrow {
            testDB.withDB { ds ->
                ds.save(validEntity)
            }
        }
    }
}