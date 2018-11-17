package org.revcrm.data.testmodels

import org.revcrm.models.BaseModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    var int_field: Int,
    var double_field: Double,
    var float_field: Float,
    var boolean_field: Boolean,
    var date_field: LocalDate,
    var time_field: LocalTime,
    var datetime_field: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var enum_field: EnumFieldOptions
): BaseModel()

@Entity
class TestModel2(
    var name: String,
    var size: Double
): BaseModel()

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

): BaseModel()
