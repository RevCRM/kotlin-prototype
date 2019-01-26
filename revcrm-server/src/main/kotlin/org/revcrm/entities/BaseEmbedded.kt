package org.revcrm.entities

import org.revcrm.annotations.APIDisabled
import org.revcrm.annotations.Label
import org.revcrm.db.EntityContext
import org.revcrm.db.WithEntityContext
import java.time.LocalDateTime
import xyz.morphia.annotations.PrePersist

abstract class BaseEmbedded : WithEntityContext {

    @Label("Date Created")
    var created_date: LocalDateTime? = null

    @Label("Last Updated")
    var updated_date: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        if (created_date == null) created_date = LocalDateTime.now()
        updated_date = LocalDateTime.now()
    }

    @Transient
    @APIDisabled
    override var context: EntityContext? = null
}