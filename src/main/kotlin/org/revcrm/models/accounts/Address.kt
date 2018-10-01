package org.revcrm.models.accounts

import org.revcrm.models.BaseModel
import javax.persistence.Entity

@Entity
class Address (
        var name: String,
        var address1: String,
        var address2: String,
        var city: String,
        var region: String,
        var postal_code: String,
        var country: String
): BaseModel()
