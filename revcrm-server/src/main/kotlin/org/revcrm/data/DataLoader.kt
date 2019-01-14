package org.revcrm.data

import com.google.gson.Gson
import org.revcrm.db.DBService
import org.revcrm.entities.Base
import org.revcrm.util.getResourceAsText

class CRMData {
    var data: List<CRMEntityData> = listOf()
}

class CRMEntityData {
    var entity: String? = null
    var data_id: String? = null
    var data: Any? = null
}

class DataLoader(private val db: DBService) {

    fun import(dataFiles: List<String>) {
        val gson = Gson()

        dataFiles.forEach { file ->
            val content = getResourceAsText(file)
            val fileData = gson.fromJson(content, CRMData::class.java)

            fileData.data.forEachIndexed { idx, data ->

                if (data.entity == null || data.data_id == null || data.data == null)
                    throw importError(file, idx, data, "entity, data_id or data missing")

                if (!db.classIsEntity(data.entity!!))
                    throw importError(file, idx, data, "not a registered entity")

                val klass = Class.forName(data.entity)

                db.withDB { ds ->

                    val record = ds.createQuery(klass)
                        .field("data_id").equalIgnoreCase(data.data_id)
                        .get()

                    if (record == null) {
                        val dataTree = gson.toJsonTree(data.data)
                        val entity = gson.fromJson(dataTree, klass)
                        (entity as Base).data_id = data.data_id
                        ds.save(entity)
                    }
                }
            }
        }
    }

    fun importError(
        file: String,
        index: Int,
        data: CRMEntityData,
        message: String
    ): Error {
        return Error("$file[$index]: Entity: ${data.entity}, ID: ${data.data_id}: $message")
    }
}