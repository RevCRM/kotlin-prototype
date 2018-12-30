package org.revcrm.entities

import org.revcrm.annotations.APIDisabled
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.IndexOptions
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.Reference

enum class AuthType {
    GOOGLE
}

@Entity
@Indexes(
    Index(fields = [
        Field("auth_type"),
        Field("auth_id")
    ], options = IndexOptions(
        unique = true
    ))
)
@APIDisabled
class RevUserAuth(
    @Reference
    var user: RevUser,
    var auth_type: AuthType,
    var auth_id: String
) : Base()
