package org.revcrm.models

import org.revcrm.annotations.APIDisabled
import xyz.morphia.annotations.Entity

enum class AuthType {
    GOOGLE
}

@Entity
//@Table(
//    indexes = [
//        Index(
//            columnList = "auth_type, auth_id"
//        )
//    ]
//)
@APIDisabled
class RevUserAuth(

    var user: RevUser,

    var auth_type: AuthType,

    var auth_id: String

) : BaseModel()
