package org.revcrm

import org.koin.dsl.module.module
import org.revcrm.data.*

val revCRMModule = module {
    single<IRevCRMDB> { RevCRMDB() }
    single<IDataLoader> { DataLoader(get()) }
}