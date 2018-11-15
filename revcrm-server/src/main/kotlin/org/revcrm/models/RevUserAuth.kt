package org.revcrm.models

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Index
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

enum class AuthType {
    GOOGLE
}

@Entity
@Table(
    indexes = [
        Index(
            columnList = "auth_type, auth_id"
        )
    ]
)
class RevUserAuth(

    @ManyToOne @NotNull
    var user: RevUser,

    @Enumerated(EnumType.STRING) @NotNull
    var auth_type: AuthType,

    @NotNull
    var auth_id: String

) : BaseModel()
