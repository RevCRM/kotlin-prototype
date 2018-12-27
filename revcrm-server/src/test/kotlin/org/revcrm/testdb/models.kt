package org.revcrm.testdb

import org.bson.types.ObjectId
import org.revcrm.annotations.APIDisabled
import org.revcrm.models.BaseModel
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

enum class EnumFieldOptions {
    OPTION1,
    OPTION2,
    OTHER_OPTION
}

@Entity
class TestFieldsModel(
    var string_field: String,
    var short_field: Short,
    var int_field: Int,
    var long_field: Long,
    var double_field: Double,
    var float_field: Float,
    var boolean_field: Boolean,
    var date_field: LocalDate,
    var time_field: LocalTime,
    var datetime_field: LocalDateTime,

    var enum_field: EnumFieldOptions
) : BaseModel()

abstract class ParentModel {
    @Id
    var id: ObjectId? = null
    var parentField: String? = null
}

@Entity
class TestModel2(
    var name: String,
    var number: Int
) : ParentModel()

@Entity
class TestConstraintsModel(
    var non_nullable_field: String,

    var nullable_field: String?,

    @field:NotEmpty
    var notempty_field: String,

    @field:NotBlank
    var notblank_field: String,

    @field:Min(10)
    var min_field: Int,

    @field:Max(100)
    var max_field: Int

) : BaseModel()

@Entity
@APIDisabled
class SensitiveModel(
    var name: String
) : BaseModel()

@Entity
class Account(
    var name: String,
    var contact: String,
    var phone: String,
    var email: String,
    var rating: Int
) : BaseModel()
