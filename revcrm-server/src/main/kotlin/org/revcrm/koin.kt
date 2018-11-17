package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.data.DBService
import org.revcrm.data.FieldService
import org.revcrm.graphql.APIService

val revCRMModule = module {
    single { DBService() }
    single { FieldService() }
    single { APIService(get(), get()) }
}