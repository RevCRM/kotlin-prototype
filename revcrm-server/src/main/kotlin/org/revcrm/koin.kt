package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.graphql.RevCRMSchema

val revCRMModule = module {
//    single<IRevCRMData> { RevCRMData() }
//    single<IDataLoader> { DataLoader(get()) }
    single { RevCRMSchema() }
}