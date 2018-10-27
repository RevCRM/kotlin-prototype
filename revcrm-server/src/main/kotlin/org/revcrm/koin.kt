package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.data.IRevCRMData
import org.revcrm.data.RevCRMData
import org.revcrm.graphql.RevCRMSchema

val revCRMModule = module {
    single<IRevCRMData> { RevCRMData() }
    single { RevCRMSchema(get()) }
}