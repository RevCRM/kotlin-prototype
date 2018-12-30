package org.revcrm.entities.accounts

import org.revcrm.entities.Base
import xyz.morphia.annotations.Entity

@Entity
class Address(
    var name: String,
    var address1: String,
    var address2: String,
    var city: String,
    var region: String,
    var postal_code: String,
    var country: String
) : Base()
