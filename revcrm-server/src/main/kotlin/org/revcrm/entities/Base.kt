package org.revcrm.entities

import org.bson.types.ObjectId
import org.revcrm.annotations.Field
import xyz.morphia.annotations.Id
import java.time.LocalDateTime
import xyz.morphia.annotations.PrePersist

abstract class Base {
    @Id
    var id: ObjectId? = null

    @Field("Date Created")
    var created_date: LocalDateTime? = null

    @Field("Last Updated")
    var updated_date: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        if (created_date == null) created_date = LocalDateTime.now()
        updated_date = LocalDateTime.now()
    }
}