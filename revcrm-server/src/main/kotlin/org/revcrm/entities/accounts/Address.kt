package org.revcrm.entities.accounts

import org.revcrm.annotations.EmbeddedEntity
import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine

@EmbeddedEntity
class Address(

    @Label("Description")
    var name: String,

    @Label("Address")
    @MultiLine
    var address: String,

    @Label("City")
    var city: String,

    @Label("Region")
    var region: String,

    @Label("Postal Code")
    var postal_code: String,

    @Label("Country")
    var country: String
) {
    val record_name: String
    get() = "${address.lines().first()}, ${this.city} (${this.name})"
}
