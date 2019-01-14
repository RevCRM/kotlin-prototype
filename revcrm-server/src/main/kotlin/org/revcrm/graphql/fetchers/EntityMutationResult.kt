package org.revcrm.graphql.fetchers

import org.revcrm.db.EntityValidationData

class EntityMutationResult<T>(
    val result: T,
    val validation: EntityValidationData
)