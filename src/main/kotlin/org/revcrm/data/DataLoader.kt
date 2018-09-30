package org.revcrm.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.revcrm.models.common.SelectionList
import javax.inject.Inject

class DataLoader @Inject constructor(_db: Database) {
    private var db = _db

    fun importData() {
        val mapper = ObjectMapper(YAMLFactory())
//        mapper.registerModule(KotlinModule())

        val res = object {}.javaClass.getResource("/data/selection_list.yml")

        val nodes = mapper.readTree(res)
        if (!nodes.isArray()) {
            throw Exception("YAML Root Element must be an array")
        }

        db.withTransaction { session ->
            for (node in nodes) {
                val dataId = node.get("dataId").asText()
                val model = node.get("model").asText()
                val label = node.get("label").asText()

                val existingRecord = session
                    .bySimpleNaturalId(SelectionList::class.java)
                    .load(dataId)

                if (existingRecord != null) {
                    println("Updating Existing Record")
                    existingRecord.model = model
                    existingRecord.label = label
                }
                else {
                    println("Creating New Record")
                    val newRecord = SelectionList(
                            model = model,
                            label = label
                    )
                    newRecord.dataId = dataId
                    session.persist(newRecord)
                }
            }
        }
    }
}