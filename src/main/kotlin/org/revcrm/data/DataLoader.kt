package org.revcrm.data

import org.revcrm.models.common.importSelectionLists
import org.revcrm.models.common.importSelectionOptions
import javax.inject.Inject

class DataLoader @Inject constructor(_db: Database) {
    private var db = _db

    fun importData() {
        importSelectionLists("/data/selection_list.yml", db)
        importSelectionOptions("/data/selection_option.yml", db)
    }
}