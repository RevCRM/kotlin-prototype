package org.revcrm.models.accounts

import org.revcrm.models.BaseModel
import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class Account(

    @Column(nullable = false)
    val name: String

): BaseModel()
