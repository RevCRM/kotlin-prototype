package org.revcrm

import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.revcrm.data.IDataLoader
import org.revcrm.data.IRevCRMDB


class RevCRM : KoinComponent {

    private val dataLoader: IDataLoader by inject()
    private val db: IRevCRMDB by inject()

    fun start() = dataLoader.importData()
    fun cleanup() = db.close()
}


fun main(args: Array<String>) {
    println("Starting RevCRM...")

    startKoin(listOf(revCRMModule))

    val revcrm = RevCRM()

    try {
        revcrm.start()
    }
    finally {
        revcrm.cleanup()
    }

}
