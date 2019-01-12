package org.revcrm.testdb

import org.bson.types.ObjectId
import org.revcrm.annotations.APIDisabled
import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
import org.revcrm.annotations.OnValidate
import org.revcrm.db.EntityValidationData
import org.revcrm.entities.Base
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

val numberOfApiDisabledEntities = 1
val testFieldsEntityDisabledFields = 2 // data_id from Base() and api_disabled_field

enum class EnumFieldOptions {
    OPTION1,
    OPTION2,
    OTHER_OPTION
}

@Entity
class TestFieldsEntity(
    @Label("Text Field")
    var string_field: String,
    @Label("Integer Field")
    var short_field: Short,
    var int_field: Int,
    var long_field: Long,
    var double_field: Double,
    var float_field: Float,
    var decimal_field: BigDecimal,
    var boolean_field: Boolean,
    var date_field: LocalDate,
    var time_field: LocalTime,
    var datetime_field: LocalDateTime,

    @APIDisabled
    var api_disabled_field: String,

    var enum_field: EnumFieldOptions,
    var related_field: TestEntity2
) : Base()

abstract class ParentEntity {
    @Id
    var id: ObjectId? = null
    var parentField: String? = null
}

@Entity
class TestEntity2(
    var name: String,
    var number: Int
) : ParentEntity()

@Entity
@TestClassValidator
class TestConstraintsEntity(
    var non_nullable_field: String,

    var nullable_field: String?,

    @MultiLine
    @get:NotBlank
    @get:Size(min = 1, max = 10)
    var textField: String,

    @field:Min(10)
    var min_field: Int,

    @field:Max(100)
    var max_field: Int

) : Base()

@Entity
class TestWithOnValidateDecorator(
    @field:Min(10)
    val numericField: Int,
    val textField: String
) : Base() {

    @OnValidate
    fun validate(validation: EntityValidationData) {
        if (textField == "invalid") {
            validation.addEntityError(this, "Fail", "textField must not be invalid!")
            validation.addEntityError(this, "Fail2", "Adding more errors, because we can :)")
        }
    }
}

@Entity
@APIDisabled
class SensitiveEntity(
    var name: String
) : Base()

@Entity
class Account(
    var name: String,
    var contact: String,
    var phone: String,
    var email: String,
    var rating: Int
) : Base()

@Entity
class TestWithEmbeddedEntity(
    var label: String,
    var options: List<TestEmbeddedEntity>?
) : Base()

@Entity
class TestEmbeddedEntity(
    var value: String
) : Base()

@Entity
class TestWithStringList(
    var values: List<String>
) : Base()
