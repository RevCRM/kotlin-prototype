package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService

val revCRMModule = module {
    single { DBService() }
    single { APIService(get()) }
}