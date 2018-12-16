package org.revcrm.models

import org.bson.types.ObjectId
import xyz.morphia.annotations.Id
import java.time.LocalDateTime
import xyz.morphia.annotations.PrePersist

abstract class BaseModel {
    @Id
    var id: ObjectId? = null

    var createdDate: LocalDateTime? = null

    var updatedDate: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        if (createdDate == null) createdDate = LocalDateTime.now()
        updatedDate = LocalDateTime.now()
    }
}