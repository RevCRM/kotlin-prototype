package org.revcrm.entities

import org.revcrm.annotations.Label
import xyz.morphia.annotations.Entity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@Entity
class RevUser(

    @Label("First Name")
    @field:NotBlank
    var first_name: String,

    @Label("Last Name")
    @field:NotBlank
    var last_name: String,

    @Label("E-mail Address")
    @field:NotBlank
    var email: String,

    var last_login: LocalDateTime? = null

) : Base()
