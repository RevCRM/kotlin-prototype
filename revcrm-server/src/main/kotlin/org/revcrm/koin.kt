package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.db.DBService
import org.revcrm.graphql.APIService
import org.revcrm.meta.MetadataService

val revCRMModule = module {
    single { DBService() }
    single { MetadataService(get()) }
    single { APIService(get(), get()) }
}