package org.revcrm.db

import com.mongodb.BasicDBObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.revcrm.testdb.TestConstraintsEntity
import org.revcrm.testdb.TestDB
import org.revcrm.testdb.TestWithValidateMethod
import org.revcrm.testdb.getTestGson

class ValidationTests {

    val testDB = TestDB.instance

    init {
        testDB.withDB { ds ->
            val col = ds.getCollection(TestConstraintsEntity::class.java)
            col.remove(BasicDBObject())
        }
    }

    val gson = getTestGson()

    @Test
    fun `EntityValidationError is raised when trying to save invalid entity fields`() {

        val invalidEntity = TestConstraintsEntity(
            non_nullable_field = "test",
            nullable_field = null,
            textField = "a string that is too long",
            min_field = -1,
            max_field = 101
        )

        val exception = assertThrows<EntityValidationError> {
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
    fun `EntityValidationError getValidationErrorData() returns expected data`() {

        val invalidEntity = TestConstraintsEntity(
            non_nullable_field = "test",
            nullable_field = null,
            textField = "a string that is too long",
            min_field = -1,
            max_field = 101
        )

        val exception = assertThrows<EntityValidationError> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        val errorData = exception.errorData

        assertThat(errorData.entityErrors).hasSize(0)
        assertThat(errorData.fieldErrors).hasSize(3)
        assertThat(errorData.fieldErrors).allMatch { err ->
            err.entity == "TestConstraintsEntity"
        }
        assertThat(errorData.fieldErrors).anyMatch { err ->
            err.fieldPath == "textField" &&
            err.code == "Size" &&
            err.message.contains("size must be between ")
        }
        assertThat(errorData.fieldErrors).anyMatch { err ->
            err.fieldPath == "min_field" &&
                err.code == "Min" &&
                err.message.contains("must be greater than or equal to ")
        }
        assertThat(errorData.fieldErrors).anyMatch { err ->
            err.fieldPath == "max_field" &&
                err.code == "Max" &&
                err.message.contains("must be less than or equal to ")
        }
    }

    @Test
    fun `EntityValidationError is raised when trying to save invalid fields + class`() {

        val invalidEntity = TestConstraintsEntity(
            non_nullable_field = "test",
            nullable_field = null,
            textField = "bad_class",
            min_field = 10,
            max_field = 999
        )

        val exception = assertThrows<EntityValidationError> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        val errorData = exception.errorData
        assertThat(errorData.fieldErrors.size).isGreaterThan(0)
        assertThat(errorData.entityErrors).hasSize(1)
        assertThat(errorData.entityErrors[0]).matches { err ->
            err.entity == "TestConstraintsEntity" &&
            err.entityPath == "" &&
            err.code == "TestClassValidator" &&
            err.message == "TestClassValidator says this entity is invalid"
        }
    }

    @Test
    fun `EntityValidationError is raised when a non-nullable field is set to null`() {

        // Use Gson to construct entity because Kotlin doesn't let us set non_nullable_field to null :)
        val tree = gson.toJsonTree(mapOf(
            "non_nullable_field" to null,
            "nullable_field" to null,
            "textField" to "short str",
            "min_field" to 10,
            "max_field" to 20
        ))
        val invalidEntity = gson.fromJson(tree, TestConstraintsEntity::class.java)

        val exception = assertThrows<EntityValidationError> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        val errorData = exception.errorData
        assertThat(errorData.entityErrors).hasSize(0)
        assertThat(errorData.fieldErrors).hasSize(1)
        assertThat(errorData.fieldErrors[0]).matches { err ->
            err.entity == "TestConstraintsEntity" &&
                err.fieldPath == "non_nullable_field" &&
                err.code == "NotNull" &&
                err.message == "must not be null"
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

    @Test
    fun `We can use our @Validate decorator for extra class-level validation`() {

        val invalidEntity = TestWithValidateMethod(
            numericField = 0,
            textField = "invalid"
        )

        val exception = assertThrows<EntityValidationError> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        val errorData = exception.errorData
        assertThat(errorData.fieldErrors).hasSize(1)
        assertThat(errorData.fieldErrors[0].fieldPath).isEqualTo("numericField")
        assertThat(errorData.entityErrors).hasSize(2)
        assertThat(errorData.entityErrors).allMatch { err ->
            err.entity == "TestWithValidateMethod"
        }
        assertThat(errorData.entityErrors).anyMatch { err -> err.code == "Fail" }
        assertThat(errorData.entityErrors).anyMatch { err -> err.code == "Fail2" }
    }

    @Test
    fun `Errors added by the @Validate-decorated method cause EntityValidationError`() {

        val invalidEntity = TestWithValidateMethod(
            numericField = 100,
            textField = "invalid"
        )

        val exception = assertThrows<EntityValidationError> {
            testDB.withDB { ds ->
                ds.save(invalidEntity)
            }
        }
        val errorData = exception.errorData
        assertThat(errorData.fieldErrors).hasSize(0)
        assertThat(errorData.entityErrors).hasSize(2)
        assertThat(errorData.entityErrors).allMatch { err ->
            err.entity == "TestWithValidateMethod"
        }
        assertThat(errorData.entityErrors).anyMatch { err -> err.code == "Fail" }
        assertThat(errorData.entityErrors).anyMatch { err -> err.code == "Fail2" }
    }

    @Test
    fun `Entity with @Validate can be valid`() {

        val validEntity = TestWithValidateMethod(
            numericField = 100,
            textField = "valid"
        )
        assertDoesNotThrow {
            testDB.withDB { ds ->
                ds.save(validEntity)
            }
        }
    }
}