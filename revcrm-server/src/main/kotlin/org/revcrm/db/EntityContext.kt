package org.revcrm.db

import org.revcrm.config.AppConfig

interface WithEntityContext {
    var context: EntityContext?
}

class EntityContext(
    val config: AppConfig
)