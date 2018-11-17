package org.revcrm.models

import org.hibernate.annotations.NaturalId
import org.revcrm.annotations.Field
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.validation.constraints.NotBlank

@Entity
class RevUser(

    @Field("First Name") @NotBlank
    var first_name: String,

    @Field("Last Name") @NotBlank
    var last_name: String,

    @Field("E-mail Address") @NotBlank
    @NaturalId
    var email: String,

    var last_login: LocalDateTime? = null

) : BaseModel()
