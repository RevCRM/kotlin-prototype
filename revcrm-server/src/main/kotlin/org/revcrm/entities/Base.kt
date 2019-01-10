package org.revcrm.entities

import org.bson.types.ObjectId
import org.revcrm.annotations.Label
import xyz.morphia.annotations.Id
import java.time.LocalDateTime
import xyz.morphia.annotations.PrePersist

abstract class Base {
    @Id
    var id: ObjectId? = null

    @Label("Date Created")
    var created_date: LocalDateTime? = null

    @Label("Last Updated")
    var updated_date: LocalDateTime? = null

    var data_id: String? = null

    @PrePersist
    fun prePersist() {
        if (created_date == null) created_date = LocalDateTime.now()
        updated_date = LocalDateTime.now()
    }
}