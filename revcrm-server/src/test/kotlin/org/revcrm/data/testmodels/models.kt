package org.revcrm.data.testmodels

import org.revcrm.models.BaseModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

enum class EnumFieldOptions {
    OPTION1,
    OPTION2,
    OTHER_OPTION
}

@Entity
class TestModel(
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