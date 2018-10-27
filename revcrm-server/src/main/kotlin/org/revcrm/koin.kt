package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.graphql.RevCRMData

val revCRMModule = module {
//    single<IRevCRMDB> { RevCRMDB() }
//    single<IDataLoader> { DataLoader(get()) }
    single { RevCRMData() }
}