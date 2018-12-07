package org.revcrm.models

import org.revcrm.annotations.APIDisabled
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Index
import javax.persistence.ManyToOne
import javax.persistence.Table

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
@APIDisabled
class RevUserAuth(

    @ManyToOne
    var user: RevUser,

    @Enumerated(EnumType.STRING)
    var auth_type: AuthType,

    var auth_id: String

) : BaseModel()
