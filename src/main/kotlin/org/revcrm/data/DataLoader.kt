package org.revcrm.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.revcrm.models.common.SelectionList
import javax.inject.Inject

data class SelectionListImport(
    val selection_list: Array<SelectionList>
//    val selection_option: Array<SelectionOption>
)

class DataLoader @Inject constructor(_db: Database, _inst: YMLHandlerInstantiator) {
    private var db = _db
    private var inst = _inst

    fun importData() {
        val mapper = ObjectMapper(YAMLFactory())
        // https://stackoverflow.com/questions/28393599/autowiring-in-jsondeserializer-springbeanautowiringsupport-vs-handlerinstantiat
        mapper.setHandlerInstantiator(inst)
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