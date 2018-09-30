package org.revcrm.models.accounts

import org.revcrm.models.BaseModel
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Account(
        var type: String,
        var tags: Array<String>,
        var code: String,
        var org_name: String,
        var title: String,
        var first_name: String,
        var last_name: String,
        var phone: String,
        var mobile: String,
        var fax: String,
        var email: String,
        var website: String,
        var notes: String,

        @ManyToOne
        @JoinColumn(name = "primary_address_id")
        var primary_address: Address

): BaseModel()
