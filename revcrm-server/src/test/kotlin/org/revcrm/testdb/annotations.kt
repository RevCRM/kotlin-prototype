package org.revcrm.testdb

import javax.validation.Constraint
import javax.validation.ConstraintValidatorContext
import javax.validation.ConstraintValidator
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TestClassValidatorImpl::class])
annotation class TestClassValidator(
    val message: String = "TestClassValidator says this entity is invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<Payload>> = []
)

class TestClassValidatorImpl : ConstraintValidator<TestClassValidator, TestConstraintsEntity> {

    override fun initialize(constraintAnnotation: TestClassValidator?) {}

    override fun isValid(entity: TestConstraintsEntity?, context: ConstraintValidatorContext): Boolean {
        return if (entity == null)
            true
        else
            entity.textField != "bad_class"
    }
}