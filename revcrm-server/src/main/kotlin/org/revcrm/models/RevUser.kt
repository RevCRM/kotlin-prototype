package org.revcrm.models

import javax.persistence.Entity

@Entity
class RevUser(
    var first_name: String,
    var last_name: String,
    var email: String
) : BaseModel()
