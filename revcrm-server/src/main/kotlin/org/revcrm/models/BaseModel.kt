package org.revcrm.models

import org.bson.types.ObjectId
import xyz.morphia.annotations.Id
import java.time.LocalDateTime
import xyz.morphia.annotations.PrePersist

abstract class BaseModel {
    @Id
    var id: ObjectId? = null

    var created_date: LocalDateTime? = null

    var updated_date: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        if (created_date == null) created_date = LocalDateTime.now()
        updated_date = LocalDateTime.now()
    }
}