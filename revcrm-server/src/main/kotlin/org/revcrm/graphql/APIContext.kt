package org.revcrm.graphql

import org.revcrm.db.DBService
import org.revcrm.meta.MetadataService

class APIContext(
    val db: DBService,
    val meta: MetadataService,
    val defaultResultsLimit: Int
)