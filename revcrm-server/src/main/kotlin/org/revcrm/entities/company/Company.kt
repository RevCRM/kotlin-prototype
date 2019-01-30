package org.revcrm.entities.company

import org.revcrm.annotations.Label
import org.revcrm.entities.Base
import xyz.morphia.annotations.Entity
import javax.validation.constraints.NotBlank

@Entity
class Company(

    @Label("Company Name")
    @field:NotBlank
    var name: String,

    @Label("Tagline")
    @field:NotBlank
    var tagline: String,

    @Label("Tax ID")
    @field:NotBlank
    var tax_id: String,

    @Label("Payment Information")
    @field:NotBlank
    var payment_information: String

) : Base()
