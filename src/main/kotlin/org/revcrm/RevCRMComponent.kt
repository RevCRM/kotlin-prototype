package org.revcrm

import dagger.Component
import org.revcrm.data.DataLoader
import org.revcrm.data.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DatabaseModule::class))
interface RevCRMComponent {
    fun getLoader(): DataLoader
}