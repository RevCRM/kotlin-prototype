package org.revcrm.entities.common

import org.revcrm.entities.Base
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity

@Entity
class SelectionList(
    var code: String,
    var label: String,
    @Embedded
    var options: List<SelectionOption>
) : Base()

@Entity
class SelectionOption(
    var code: String,
    var label: String
) : Base()
