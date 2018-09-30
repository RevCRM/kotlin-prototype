package org.revcrm.data

import org.revcrm.models.common.importSelectionLists
import javax.inject.Inject

class DataLoader @Inject constructor(_db: Database) {
    private var db = _db

    fun importData() {
        importSelectionLists("/data/selection_list.yml", db)
    }
}