package org.revcrm.data

import org.revcrm.models.common.importSelectionLists
import org.revcrm.models.common.importSelectionOptions

interface IDataLoader {
    fun importData()
}

class DataLoader (private val db: IRevCRMDB): IDataLoader {

    override fun importData() {
        importSelectionLists("/data/selection_list.yml", db)
        importSelectionOptions("/data/selection_option.yml", db)
    }
}