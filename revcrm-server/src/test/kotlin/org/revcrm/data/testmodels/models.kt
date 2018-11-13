package org.revcrm.data.testmodels

import org.revcrm.models.BaseModel
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
    var name: String,
    var age: Int,
    var length: Float,
    var is_awesome: Boolean,

    @Enumerated(EnumType.STRING)
    var enum_field: EnumFieldOptions
): BaseModel()

@Entity
class TestModel2(
    var name: String,
    var size: Double
): BaseModel()