package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.data.RevCRMData
import org.revcrm.graphql.RevCRMSchema

val revCRMModule = module {
    single { RevCRMData() }
    single { RevCRMSchema(get()) }
}