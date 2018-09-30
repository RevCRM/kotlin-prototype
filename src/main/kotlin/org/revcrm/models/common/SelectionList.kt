package org.revcrm.models.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.revcrm.data.Database
import org.revcrm.models.BaseModel
import javax.persistence.Entity

@Entity
data class SelectionList (
        var model: String,
        var label: String
): BaseModel()

fun importSelectionLists(fileName: String, db: Database) {
    val mapper = ObjectMapper(YAMLFactory())
    val res = object {}.javaClass.getResource(fileName)
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
                existingRecord.model = model
                existingRecord.label = label
            }
            else {
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