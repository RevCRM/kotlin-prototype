package org.revcrm.data.custom

import org.hibernate.boot.MetadataSources
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun registerAccountC(metadata: MetadataSources) {

    // Lets try adding a custom entity
    // http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd

    val doc = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument()

    val mappings = doc.createElement("hibernate-mapping")
    doc.appendChild(mappings)

    val accountCustom = doc.createElement("class")
    accountCustom.setAttribute("entity-name", "account_c")
    mappings.appendChild(accountCustom)
    val idField = doc.createElement("id")
    idField.setAttribute("name", "accountId")
    idField.setAttribute("type", "int")
//    idField.setAttribute("lazy", "true")
    accountCustom.appendChild(idField)
        val idGenerator = doc.createElement("generator")
        idGenerator.setAttribute("class", "foreign")
        idField.appendChild(idGenerator)
            val genParam = doc.createElement("param")
            genParam.setAttribute("name", "property")
            genParam.setTextContent("account")
            idGenerator.appendChild(genParam)
    val relField = doc.createElement("one-to-one")
    relField.setAttribute("name", "account")
    relField.setAttribute("class", "org.revcrm.models.accounts.Account")
    relField.setAttribute("constrained", "true")
    accountCustom.appendChild(relField)
    val nameField = doc.createElement("property")
    nameField.setAttribute("name", "custom_name")
    nameField.setAttribute("type", "string")
    accountCustom.appendChild(nameField)

    val outputStream = ByteArrayOutputStream()

    val xmlTransformer = TransformerFactory.newInstance().newTransformer()
    xmlTransformer.transform(DOMSource(doc), StreamResult(outputStream))

    metadata.addInputStream(ByteArrayInputStream(outputStream.toByteArray()))
}