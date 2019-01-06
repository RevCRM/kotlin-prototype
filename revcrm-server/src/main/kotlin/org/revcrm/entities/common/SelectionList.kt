package org.revcrm.entities.common

import org.revcrm.entities.Base
import xyz.morphia.annotations.Embedded
import xyz.morphia.annotations.Entity

@Entity
class SelectionList(
    var code: String,
    var label: String,
    @Embedded
    var options: List<SelectionOption>
) : Base()

class SelectionOption(
    var code: String,
    var label: String
) : Base()

// fun importSelectionLists(fileName: String, db: DBService) {
//    val mapper = ObjectMapper(YAMLFactory())
//    val res = object {}.javaClass.getResource(fileName)
//    val nodes = mapper.readTree(res)
//    if (!nodes.isArray()) {
//        throw Exception("YAML Root Element must be an array")
//    }
//    db.withTransaction { em ->
//        for (node in nodes) {
//            val code = node.get("code").asText()
//            val model = node.get("model").asText()
//            val label = node.get("label").asText()
//
//            val existingRecord = em.session
//                .bySimpleNaturalId(SelectionList::class.java)
//                .load(code)
//
//            if (existingRecord != null) {
//                existingRecord.model = model
//                existingRecord.label = label
//            } else {
//                val newRecord = SelectionList(
//                    code = code,
//                    model = model,
//                    label = label
//                )
//                em.persist(newRecord)
//            }
//        }
//    }
// }