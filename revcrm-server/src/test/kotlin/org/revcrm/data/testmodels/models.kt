package org.revcrm.data.testmodels

import org.revcrm.models.BaseModel
import javax.persistence.Entity

@Entity
class TestModel(
    var name: String,
    var age: Int,
    var length: Float,
    var is_awesome: Boolean
): BaseModel()

@Entity
class TestModel2(
    var name: String,
    var size: Double
): BaseModel()