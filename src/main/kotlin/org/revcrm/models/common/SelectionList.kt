package org.revcrm.models.common

import org.revcrm.models.BaseModel
import javax.persistence.Entity

@Entity
data class SelectionList (
        var model: String,
        var label: String
): BaseModel()
