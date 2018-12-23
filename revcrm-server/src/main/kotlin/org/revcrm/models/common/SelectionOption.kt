package org.revcrm.models.common

import org.revcrm.models.BaseModel
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Reference



//fun importSelectionOptions(fileName: String, db: DBService) {
//    val mapper = ObjectMapper(YAMLFactory())
//    val res = object {}.javaClass.getResource(fileName)
//    val nodes = mapper.readTree(res)
//    if (!nodes.isArray()) {
//        throw Exception("YAML Root Element must be an array")
//    }
//    db.withTransaction { em ->
//        for (node in nodes) {
//            val list = node.get("list").asText()
//            val code = node.get("code").asText()
//            val label = node.get("label").asText()
//            val seq = node.get("seq")?.asInt()?.toShort() ?: 1
//
//            val listRecord = em.session
//                .bySimpleNaturalId(SelectionList::class.java)
//                .load(list)
//            if (listRecord == null) {
//                throw Exception("List '$list' not found.")
//            }
//
//            val existingRecord = em.session
//                .bySimpleNaturalId(SelectionOption::class.java)
//                .load(code)
//
//            if (existingRecord != null) {
//                existingRecord.list = listRecord
//                existingRecord.label = label
//                existingRecord.seq = seq
//            } else {
//                val newRecord = SelectionOption(
//                    list = listRecord,
//                    code = code,
//                    label = label,
//                    seq = seq
//                )
//                em.persist(newRecord)
//            }
//        }
//    }
//}
