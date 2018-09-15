package org.revcrm.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.revcrm.models.common.SelectionList
import org.revcrm.models.common.SelectionOption
import javax.inject.Inject

data class SelectionListImport(
    val selection_list: Array<SelectionList>,
    val selection_option: Array<SelectionOption>
)

class DataLoader @Inject constructor(_db: Database) {
    private var db: Database = _db

    fun importData() {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())

        val res = object {}.javaClass.getResource("/data/selections.yml")

        val import = mapper.readValue<SelectionListImport>(res)

        db.withTransaction { session ->
            import.selection_list.forEach { list ->
                session.save(list)
            }
        }

        println("imported ${import.selection_list.size} lists")
    }
}