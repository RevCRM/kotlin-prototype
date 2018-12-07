package org.revcrm.models.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.hibernate.annotations.NaturalId
import org.revcrm.data.DBService
import org.revcrm.models.BaseModel
import org.revcrm.util.session
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class SelectionOption(
    @ManyToOne
    var list: SelectionList,
    @NaturalId
    var code: String,
    var label: String,
    var seq: Short
) : BaseModel()

fun importSelectionOptions(fileName: String, db: DBService) {
    val mapper = ObjectMapper(YAMLFactory())
    val res = object {}.javaClass.getResource(fileName)
    val nodes = mapper.readTree(res)
    if (!nodes.isArray()) {
        throw Exception("YAML Root Element must be an array")
    }
    db.withTransaction { em ->
        for (node in nodes) {
            val list = node.get("list").asText()
            val code = node.get("code").asText()
            val label = node.get("label").asText()
            val seq = node.get("seq")?.asInt()?.toShort() ?: 1

            val listRecord = em.session
                .bySimpleNaturalId(SelectionList::class.java)
                .load(list)
            if (listRecord == null) {
                throw Exception("List '$list' not found.")
            }

            val existingRecord = em.session
                .bySimpleNaturalId(SelectionOption::class.java)
                .load(code)

            if (existingRecord != null) {
                existingRecord.list = listRecord
                existingRecord.label = label
                existingRecord.seq = seq
            } else {
                val newRecord = SelectionOption(
                    list = listRecord,
                    code = code,
                    label = label,
                    seq = seq
                )
                em.persist(newRecord)
            }
        }
    }
}
