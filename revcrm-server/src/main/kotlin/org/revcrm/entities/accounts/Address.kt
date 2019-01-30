package org.revcrm.entities.accounts

import org.revcrm.annotations.EmbeddedEntity
import org.revcrm.annotations.Label
import org.revcrm.annotations.MultiLine
import javax.validation.constraints.NotBlank

@EmbeddedEntity
class Address(

    @Label("Description")
    @field:NotBlank
    var name: String,

    @Label("Address")
    @MultiLine
    var address: String?,

    @Label("City")
    var city: String?,

    @Label("Region")
    var region: String?,

    @Label("Postal Code")
    var postal_code: String?,

    @Label("Country")
    var country: String?
) {
    val record_name: String
        get() = "${address?.lines()?.first() ?: "No Address"}, ${this.city} (${this.name})"

    val full_address: String
        get() = """
            $address
            $city, $postal_code
            $country
        """.trimIndent()
}