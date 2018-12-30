package org.revcrm.entities

import org.bson.types.ObjectId
import xyz.morphia.annotations.Id
import java.time.OffsetDateTime
import xyz.morphia.annotations.PrePersist

abstract class Base {
    @Id
    var id: ObjectId? = null

    var created_date: OffsetDateTime? = null

    var updated_date: OffsetDateTime? = null

    @PrePersist
    fun prePersist() {
        if (created_date == null) created_date = OffsetDateTime.now()
        updated_date = OffsetDateTime.now()
    }
}