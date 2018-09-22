package org.revcrm.models.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.revcrm.data.Database
import org.revcrm.models.BaseModel
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne


@Entity
data class SelectionList(
    val model: String,
    val label: String
): BaseModel()

@Entity
@JsonDeserialize(using = SelectionOptionDeserializer::class)
data class SelectionOption(

    @ManyToOne
    @JoinColumn(name = "listId")
    val list: SelectionList,
    val code: String,
    val label: String,
    val seq: Short

): BaseModel()

class SelectionOptionDeserializer : JsonDeserializer<SelectionOption>() {
//    val db: Database = _db

    override fun deserialize(jp: JsonParser, ctx: DeserializationContext): SelectionOption {
        val db = ctx.getAttribute("db") as Database
        val node = jp.codec.readTree<JsonNode>(jp)

        val code = node.get("code").asText()
        val label = node.get("label").asText()
        val seq = node.get("seq").asInt().toShort()

        val listId = node.get("list").asText()
        println("Looking for list id '${listId}'")

        val list = db.withTransaction {session ->
            session.createQuery("""
                    from SelectionList
                    where dataId = :listId
                """)
                .setParameter("listId", listId)
                .uniqueResult()
        }
        println("Got List ${list}")

        val dummyList = SelectionList(model = "test", label = "test")
        return SelectionOption(dummyList, code, label, seq)
    }
}