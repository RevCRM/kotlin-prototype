package org.revcrm.entities

import org.revcrm.annotations.Label
import xyz.morphia.annotations.Entity
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@Entity
class RevUser(

    @Label("First Name") @NotBlank
    var first_name: String,

    @Label("Last Name") @NotBlank
    var last_name: String,

    @Label("E-mail Address") @NotBlank
    var email: String,

    var last_login: LocalDateTime? = null

) : Base()
