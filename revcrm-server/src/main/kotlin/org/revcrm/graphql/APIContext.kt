package org.revcrm.graphql

import org.revcrm.db.DBService

class APIContext(
    val db: DBService,
    val defaultResultsLimit: Int
)